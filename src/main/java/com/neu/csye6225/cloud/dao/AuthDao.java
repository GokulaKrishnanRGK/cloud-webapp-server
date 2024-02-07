package com.neu.csye6225.cloud.dao;

import com.neu.csye6225.cloud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("authDao")
public class AuthDao {

  Logger logger = LoggerFactory.getLogger(AuthDao.class);

  @Autowired
  private SessionFactory sessionFactory;

  public User getUserCredential(String id) {
    String hql = "SELECT user.id, user.username, user.password FROM User user WHERE user.id=:id";

    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(hql, User.class).setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      logger.error("Exception in getUserCredential: ", e);
    }
    return null;
  }

}
