package com.neu.csye6225.cloud.model;

import com.neu.csye6225.cloud.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "`user`")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "password")
  private String password;

  @Column(name = "username")
  private String username;

  @Column(name = "account_created")
  private Timestamp accountCreated;

  @Column(name = "account_updated")
  private Timestamp accountUpdated;

  public User() {
  }

  public User(String firstname, String lastname, String password, String username) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.password = password;
    this.username = username;
  }

  public User(UserDto userCreateDto) {
    this.setFirstname(userCreateDto.getFirstname());
    this.setLastname(userCreateDto.getLastname());
    this.setPassword(userCreateDto.getPassword());
    this.setUsername(userCreateDto.getUsername());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Timestamp getAccountCreated() {
    return accountCreated;
  }

  public void setAccountCreated(Timestamp accountCreated) {
    this.accountCreated = accountCreated;
  }

  public Timestamp getAccountUpdated() {
    return accountUpdated;
  }

  public void setAccountUpdated(Timestamp accountUpdated) {
    this.accountUpdated = accountUpdated;
  }
}
