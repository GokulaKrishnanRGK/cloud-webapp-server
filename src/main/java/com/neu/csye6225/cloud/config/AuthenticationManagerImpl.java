package com.neu.csye6225.cloud.config;

import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationManagerImpl implements AuthenticationManager {

  private PasswordEncoder passwordEncoder;
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String enteredPassword = (String) authentication.getCredentials();
    User udl = userService.loadByUsername(username);
    if (udl == null) {
      throw new BadCredentialsException("invalidUser");
    }
    String storedPwd = udl.getPassword();
    if (!passwordEncoder.matches(enteredPassword, storedPwd)) {
      throw new BadCredentialsException("Invalid password");
    }
    return new UsernamePasswordAuthenticationToken(udl, udl.getPassword());
  }

  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
