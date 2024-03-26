package com.neu.csye6225.cloud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="verify")
public class Verify {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="token")
  private String token;

  @Column(name = "is_verified")
  private boolean isVerified;

  @Column(name = "generated_time")
  private Timestamp generatedTime;

  @OneToOne
  private User user;

  public Verify() {
  }

  public Verify(String token) {
    this.token = token;
    this.isVerified = false;
    this.generatedTime = new Timestamp(System.currentTimeMillis());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isVerified() {
    return isVerified;
  }

  public void setVerified(boolean verified) {
    isVerified = verified;
  }

  public Timestamp getGeneratedTime() {
    return generatedTime;
  }

  public void setGeneratedTime(Timestamp generatedTime) {
    this.generatedTime = generatedTime;
  }
}
