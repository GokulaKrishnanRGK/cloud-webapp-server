package com.neu.csye6225.cloud.entity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class AuthFacade {

  private String username;
  private String password;

  private AuthFacade(String userName, String password) {
    this.username = userName;
    this.password = password;
  }

  public static AuthFacade getAuthFacade(String authHeader) {
    if (StringUtils.hasLength(authHeader) && authHeader.toUpperCase().startsWith("BASIC")) {
      String credential = authHeader.substring(5).trim();
      String[] credentials = new String(Base64.getDecoder().decode(credential), StandardCharsets.UTF_8).split(":", 2);
      return new AuthFacade(credentials[0], credentials[1]);
    }
    return null;
  }

  public String getUsername() {
    return username;
  }

  public boolean isPassValid(String userPass, PasswordEncoder encoder) {
    return encoder.matches(this.password, userPass);
  }

}
