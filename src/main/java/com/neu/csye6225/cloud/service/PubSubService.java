package com.neu.csye6225.cloud.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("pubSubService")
public class PubSubService {

  private static final Logger logger = LoggerFactory.getLogger(PubSubService.class);

  private PubSubTemplate pubSubTemplate;

  public PubSubService(PubSubTemplate pubSubTemplate) {
    this.pubSubTemplate = pubSubTemplate;
  }

  public void publishMessage(String topicName, String message) {
    this.pubSubTemplate.publish(topicName, message);
  }
}
