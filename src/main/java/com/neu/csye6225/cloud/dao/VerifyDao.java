package com.neu.csye6225.cloud.dao;

import com.neu.csye6225.cloud.model.User;
import com.neu.csye6225.cloud.model.Verify;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("verifyDao")
public class VerifyDao {

  private final static Logger logger = LoggerFactory.getLogger(VerifyDao.class);

  @Autowired
  private SessionFactory sessionFactory;

  public void createVerify(Verify verify) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.persist(verify);
      transaction.commit();
    } catch (Exception ignored) {
      if (transaction != null) {
        transaction.rollback();
      }
      logger.error("Exception: ", ignored);
    }
  }

  public Verify getByToken(String token) {
    try (Session session = sessionFactory.openSession()) {
      String query = "from Verify where token = :val";
      return session.createQuery(query, Verify.class).setParameter("val", token).uniqueResult();
    } catch (Exception e) {
      logger.error("Exception while get by token: ", e);
    }
    return null;
  }

  public void updateVerify(Verify verify) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.merge(verify);
      transaction.commit();
    } catch (Exception ignored) {
      if (transaction != null) {
        transaction.rollback();
      }
      logger.error("Exception: ", ignored);
    }
  }

}
