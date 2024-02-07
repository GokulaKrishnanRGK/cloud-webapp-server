package com.neu.csye6225.cloud.dao;

import com.neu.csye6225.cloud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao {

  private final static Logger logger = LoggerFactory.getLogger(UserDao.class);

  @Autowired
  private SessionFactory sessionFactory;

  public User getUserById(String id) {
    String hql = "FROM User user WHERE user.id = :id";
    try (Session session = this.sessionFactory.openSession()) {
      return session.createQuery(hql, User.class).setParameter("id", id).getSingleResult();
    } catch (Exception ignored) {
      logger.error("Exception: ", ignored);
      return null;
    }
  }

  public void createUser(User user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.persist(user);
      transaction.commit();
    } catch (Exception ignored) {
      if (transaction != null) {
        transaction.rollback();
      }
      logger.error("Exception: ", ignored);
    }
  }

  public void updateUser(User user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.merge(user);
      transaction.commit();
    } catch (Exception ignored) {
      if (transaction != null) {
        transaction.rollback();
      }
      logger.error("Exception: ", ignored);
    }
  }

  public boolean checkUsernameExist(String username) {
    String hql = "SELECT COUNT(user.id) FROM User user WHERE user.username = :username";
    try (Session session = this.sessionFactory.openSession()) {
      return session.createQuery(hql, Long.class).setParameter("username", username).getSingleResult() > 0;
    } catch (Exception ignored) {
      logger.error("Exception: ", ignored);
      return false;
    }
  }

  public User loadByUsername(String username) {
    String hql = "from User user WHERE user.username = :username";
    try (Session session = this.sessionFactory.openSession()) {
      return session.createQuery(hql, User.class).setParameter("username", username).getSingleResult();
    } catch (Exception ignored) {
      logger.error("Exception: ", ignored);
      return null;
    }
  }

}
