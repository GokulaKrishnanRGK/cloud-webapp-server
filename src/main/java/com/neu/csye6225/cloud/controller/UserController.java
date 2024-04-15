package com.neu.csye6225.cloud.controller;

import com.google.gson.Gson;
import com.neu.csye6225.cloud.dto.UserDto;
import com.neu.csye6225.cloud.entity.AppResponse;
import com.neu.csye6225.cloud.entity.AuthFacade;
import com.neu.csye6225.cloud.entity.EventArtifact;
import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.model.Verify;
import com.neu.csye6225.cloud.service.PubSubService;
import com.neu.csye6225.cloud.service.UserService;
import com.neu.csye6225.cloud.service.VerifyService;
import com.neu.csye6225.cloud.util.MiscUtil;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v2/user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private Gson gson = new Gson();

  @Autowired
  private UserService userService;

  @Autowired
  private VerifyService verifyService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PubSubService pubSubService;

  @GetMapping(value = "/self")
  public ResponseEntity<String> getUser(@RequestHeader("Authorization") String authHeader) {
    AppResponse appResponse;
    AuthFacade authFacade = AuthFacade.getAuthFacade(authHeader);
    User authUser = this.userService.loadByUsername(authFacade.getUsername());
    logger.debug("Get user: {}", authFacade.getUsername());
    if (!validateUser(authFacade, authUser)) {
      appResponse = new AppResponse(HttpStatus.UNAUTHORIZED);
      logger.error("Get User: Unauthorized");
    } else {
      if (!authUser.getVerify().isVerified()) {
        appResponse = new AppResponse(HttpStatus.FORBIDDEN);
        logger.error("Update user: user not verified, forbidden");
      } else {
        authUser.setPassword(null);
        authUser.setVerify(null);
        appResponse = new AppResponse(HttpStatus.OK, authUser);
        logger.info("Get User success: {}", authUser.getUsername());
      }
    }
    return appResponse.getResponseEntity();
  }

  @PutMapping(value = "/self")
  public ResponseEntity<String> updateUser(@RequestBody UserDto userDto, @RequestHeader("Authorization") String authHeader) {
    AppResponse appResponse;
    AuthFacade authFacade = AuthFacade.getAuthFacade(authHeader);
    User authUser = this.userService.loadByUsername(authFacade.getUsername());
    logger.debug("Update user: {}", authFacade.getUsername());
    if (!validateUser(authFacade, authUser)) {
      appResponse = new AppResponse(HttpStatus.UNAUTHORIZED);
      logger.error("Update user: Unauthorized");
    } else {
      if (!authUser.getVerify().isVerified()) {
        appResponse = new AppResponse(HttpStatus.FORBIDDEN);
        logger.error("Update user: user not verified, forbidden");
      } else {
        if (userDto.getUsername() != null && !authUser.getUsername().equals(userDto.getUsername())) {
          appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Cannot update username");
          logger.warn("Update user: Cannot update username");
        } else if (!((userDto.getPassword() == null || StringUtils.hasLength(userDto.getPassword().trim())) && (userDto.getFirstname()
            == null || StringUtils.hasLength(userDto.getFirstname().trim())) && (userDto.getLastname() == null || StringUtils.hasLength(
            userDto.getLastname().trim())))) {
          appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Empty value");
          logger.warn("Update user: Empty value");
        } else {
          if (!userDto.isUserSame(authUser) || !this.passwordEncoder.matches(userDto.getPassword(), authUser.getPassword())) {
            authUser.setFirstname(userDto.getFirstname());
            authUser.setLastname(userDto.getLastname());
            authUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            this.userService.persistUser(authUser);
            logger.info("Update user success: {}", authUser.getUsername());
          }
          appResponse = new AppResponse(HttpStatus.NO_CONTENT);
        }
      }
    }
    return appResponse.getResponseEntity();
  }

  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    AppResponse appResponse;
    logger.debug("Create user API");
    if (!userDto.isValid()) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Missing parameters");
      logger.warn("Create user: Missing parameters");
    } else if (!MiscUtil.isValidEmail(userDto.getUsername())) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Invalid Email input");
      logger.warn("Create user: Invalid Email input");
    } else if (this.userService.checkUsernameExist(userDto.getUsername())) {
      logger.warn("Create user: User - Email already exist");
      User user = this.userService.loadByUsername(userDto.getUsername());
      if (user.getVerify().isVerified()) {
        appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "User - Email already exist");
        logger.warn("Create user: Verified user");
      } else {
        logger.warn("Verification link triggered again for user");
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.getVerify().setToken(UUID.randomUUID().toString());
        user.getVerify().setGeneratedTime(new Timestamp(System.currentTimeMillis()));
        this.userService.persistUser(user);
        this.verifyService.updateVerify(user.getVerify());
        EventArtifact eventArtifact = new EventArtifact(user.getUsername(), user.getVerify().getToken());
        pubSubService.publishMessage("verify_email", gson.toJson(eventArtifact));
        user.setPassword(null);
        user.setVerify(null);
        logger.info("Create user success: {}", user.getUsername());
        appResponse = new AppResponse(HttpStatus.CREATED, user);
      }
    } else {
      User user = new User(userDto);
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
      this.userService.createUser(user);
      Verify verify = new Verify(UUID.randomUUID().toString());
      verify.setUser(user);
      this.verifyService.createVerify(verify);
      user.setPassword(null);
      user.setVerify(null);
      EventArtifact eventArtifact = new EventArtifact(user.getUsername(), verify.getToken());
      pubSubService.publishMessage("verify_email", gson.toJson(eventArtifact));
      appResponse = new AppResponse(HttpStatus.CREATED, user);
      logger.info("Create user success: {}", user.getUsername());
    }
    return appResponse.getResponseEntity();
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<String> verifyUser(@RequestParam String token) {
    AppResponse appResponse;
    logger.debug("Verify user API");
    if (token == null || !StringUtils.hasLength(token.trim())) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Empty token");
      logger.warn("Verify user: Empty token");
    } else {
      Verify verify = this.verifyService.getByToken(token);
      if (verify == null) {
        appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Invalid token");
        logger.warn("Verify user: Invalid token");
      } else {
        if (isTokenExpired(verify.getGeneratedTime())) {
          appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Token Expired");
          logger.warn("Verify user: Token Expired");
        } else {
          verify.setVerified(true);
          this.verifyService.updateVerify(verify);
          appResponse = new AppResponse(HttpStatus.ACCEPTED, "User verified");
          logger.warn("Verify user: User verified");
        }
      }
    }
    return appResponse.getResponseEntity();
  }

  private boolean validateUser(AuthFacade authFacade, User currentUser) {
    return currentUser != null && authFacade.isPassValid(currentUser.getPassword(), passwordEncoder);
  }

  public static boolean isTokenExpired(Timestamp timestampToCheck) {
    Instant now = Instant.now();
    Duration duration = Duration.between(timestampToCheck.toInstant(), now);
    return duration.toMinutes() > 2;
  }

}
