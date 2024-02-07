package com.neu.csye6225.cloud.service;

import com.neu.csye6225.cloud.dao.AuthDao;
import com.neu.csye6225.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private AuthDao authDao;

  public User getUserCredential(String id) {
    return this.authDao.getUserCredential(id);
  }

}
