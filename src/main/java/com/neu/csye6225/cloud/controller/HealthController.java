package com.neu.csye6225.cloud.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

  @Autowired
  private SessionFactory sessionFactory;

  @RequestMapping(value = "/healthz", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> getDbHealth(HttpServletRequest request) throws IOException {
    int statusCode;
    if (!request.getMethod().equals("GET")) {
      statusCode = 405;
    } else if (!request.getParameterMap().isEmpty() || request.getReader().ready()) {
      statusCode = 400;
    } else {
      try (Session session = sessionFactory.openSession()) {
        session.createNativeQuery("SELECT 1").getSingleResult();
        statusCode = 200;
      } catch (Exception e) {
        statusCode = 503;
        logger.error("DB Health check failed with exception, ", e);
      }
    }
    logger.info("Health check status: HTTP {}", statusCode);
    return ResponseEntity.status(statusCode).cacheControl(CacheControl.noCache()).cacheControl(CacheControl.noStore()).build();
  }

}