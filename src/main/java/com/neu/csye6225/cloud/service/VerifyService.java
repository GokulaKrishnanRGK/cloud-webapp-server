package com.neu.csye6225.cloud.service;

import com.neu.csye6225.cloud.dao.VerifyDao;
import com.neu.csye6225.cloud.model.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

  @Autowired
  private VerifyDao verifyDao;

  public void createVerify(Verify verify) {
    this.verifyDao.createVerify(verify);
  }

  public Verify getByToken(String token) {
    return this.verifyDao.getByToken(token);
  }

  public void updateVerify(Verify verify) {
    this.verifyDao.updateVerify(verify);
  }

}
