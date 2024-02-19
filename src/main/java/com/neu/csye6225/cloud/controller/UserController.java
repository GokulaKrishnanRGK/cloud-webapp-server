package com.neu.csye6225.cloud.controller;

import com.neu.csye6225.cloud.dto.UserDto;
import com.neu.csye6225.cloud.entity.AppResponse;
import com.neu.csye6225.cloud.entity.AuthFacade;
import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.service.UserService;
import com.neu.csye6225.cloud.util.MiscUtil;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping(value = "/self")
  public ResponseEntity<String> getUser(@RequestHeader("Authorization") String authHeader) {
    AppResponse appResponse;
    AuthFacade authFacade = AuthFacade.getAuthFacade(authHeader);
    User authUser = this.userService.loadByUsername(authFacade.getUsername());
    if (!validateUser(authFacade, authUser)) {
      appResponse = new AppResponse(HttpStatus.UNAUTHORIZED);
    } else {
      authUser.setPassword(null);
      appResponse = new AppResponse(HttpStatus.OK, authUser);
    }
    return appResponse.getResponseEntity();
  }

  @PutMapping(value = "/self")
  public ResponseEntity<String> updateUser(@RequestBody UserDto userDto, @RequestHeader("Authorization") String authHeader) {
    AppResponse appResponse;
    AuthFacade authFacade = AuthFacade.getAuthFacade(authHeader);
    User authUser = this.userService.loadByUsername(authFacade.getUsername());
    if (!validateUser(authFacade, authUser)) {
      appResponse = new AppResponse(HttpStatus.UNAUTHORIZED);
    } else {
      if (!authUser.getUsername().equals(userDto.getUsername())) {
        appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Cannot update username");
      } else if (!((userDto.getPassword() == null || StringUtils.hasLength(userDto.getPassword().trim())) && (userDto.getFirstname() == null
          || StringUtils.hasLength(userDto.getFirstname().trim())) && (userDto.getLastname() == null || StringUtils.hasLength(
          userDto.getLastname().trim())))) {
        appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Empty value");
      } else {
        if (!userDto.isUserSame(authUser) || !this.passwordEncoder.matches(userDto.getPassword(), authUser.getPassword())) {
          authUser.setFirstname(userDto.getFirstname());
          authUser.setLastname(userDto.getLastname());
          authUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
          this.userService.persistUser(authUser);
        }
        appResponse = new AppResponse(HttpStatus.OK);
      }
    }
    return appResponse.getResponseEntity();
  }

  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    AppResponse appResponse;
    if (!userDto.isValid()) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Missing parameters");
    } else if (!MiscUtil.isValidEmail(userDto.getUsername())) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "Invalid Email input");
    } else if (this.userService.checkUsernameExist(userDto.getUsername())) {
      appResponse = new AppResponse(HttpStatus.BAD_REQUEST, "User - Email already exist");
    } else {
      User user = new User(userDto);
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
      this.userService.createUser(user);
      user.setPassword(null);
      appResponse = new AppResponse(HttpStatus.CREATED, user);
    }
    return appResponse.getResponseEntity();
  }

  private boolean validateUser(AuthFacade authFacade, User currentUser) {
    return currentUser != null && authFacade.isPassValid(currentUser.getPassword(), passwordEncoder);
  }

}
