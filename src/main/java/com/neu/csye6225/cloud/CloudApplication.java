package com.neu.csye6225.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CloudApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudApplication.class, args);
  }

}
