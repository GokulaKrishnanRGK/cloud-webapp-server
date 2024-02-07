package com.neu.csye6225.cloud.service;

import com.neu.csye6225.cloud.dao.UserDao;
import com.neu.csye6225.cloud.model.User;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserDao userDao;

  public User getUserById(String id) {
    return this.userDao.getUserById(id);
  }

  public void createUser(User user) {
    user.setAccountCreated(new Timestamp(System.currentTimeMillis()));
    user.setAccountUpdated(new Timestamp(System.currentTimeMillis()));
    this.userDao.createUser(user);
  }

  public void persistUser(User user) {
    user.setAccountUpdated(new Timestamp(System.currentTimeMillis()));
    this.userDao.updateUser(user);
  }

  public boolean checkUsernameExist(String username) {
    return this.userDao.checkUsernameExist(username);
  }

  public User loadByUsername(String username) {
    return this.userDao.loadByUsername(username);
  }

}
