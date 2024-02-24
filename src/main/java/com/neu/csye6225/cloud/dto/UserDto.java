package com.neu.csye6225.cloud.dto;

import com.neu.csye6225.cloud.model.User;
import org.springframework.util.StringUtils;

public class UserDto {

  private String username;
  private String firstname;
  private String lastname;
  private String password;

  public boolean isValid() {
    return this.username != null
        && StringUtils.hasLength(this.username.trim())
        && this.firstname != null
        && StringUtils.hasLength(this.firstname.trim())
        && this.lastname != null
        && StringUtils.hasLength(this.lastname.trim())
        && this.password != null
        && StringUtils.hasLength(this.password.trim());
  }

  public boolean isUserSame(User user) {
    return this.firstname.equals(user.getUsername()) && this.lastname.equals(user.getLastname());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
