package com.neu.csye6225.cloud.service.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.Objects;

@Profile("aws")
@Service("pubSubService")
public class AwsPubSubService implements IPubSubService {

  private static final Logger logger = LoggerFactory.getLogger(AwsPubSubService.class);

  private final SnsClient snsClient;

  public AwsPubSubService(SnsClient snsClient) {
    this.snsClient = snsClient;
  }

  @Override
  public void publishMessage(String topicArn, String message) {
    Objects.requireNonNull(topicArn, "topicNameOrArn must not be null");
    Objects.requireNonNull(message, "message must not be null");

    try {
      PublishRequest req = PublishRequest.builder()
          .topicArn(topicArn)
          .message(message)
          .build();

      PublishResponse resp = snsClient.publish(req);
      logger.info("SNS publish success topicArn={} messageId={}", topicArn, resp.messageId());

    } catch (SnsException e) {
      logger.error("SNS publish failed topicArn={} awsErrorCode={} message={}",
          topicArn,
          e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "unknown",
          e.getMessage(),
          e);
      throw e;
    }
  }

  private String ensureTopicArn(String topicName) {
    try {
      CreateTopicResponse resp = snsClient.createTopic(
          CreateTopicRequest.builder().name(topicName).build()
      );
      logger.info("SNS topic ensured name={} topicArn={}", topicName, resp.topicArn());
      return resp.topicArn();
    } catch (SnsException e) {
      logger.error("SNS createTopic/ensure failed name={} message={}", topicName, e.getMessage(), e);
      throw e;
    }
  }

  private boolean isArn(String value) {
    return value.startsWith("arn:aws:sns:");
  }
}
