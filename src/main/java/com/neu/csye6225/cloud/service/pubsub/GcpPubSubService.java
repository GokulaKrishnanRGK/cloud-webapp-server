package com.neu.csye6225.cloud.service.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("gcp")
@Service("pubSubService")
public class GcpPubSubService implements IPubSubService {

  private final Logger logger = LoggerFactory.getLogger(GcpPubSubService.class);

  private PubSubTemplate pubSubTemplate;

  public GcpPubSubService(PubSubTemplate pubSubTemplate) {
    this.pubSubTemplate = pubSubTemplate;
  }

  public void publishMessage(String topicName, String message) {
    this.pubSubTemplate.publish(topicName, message);
  }
}
