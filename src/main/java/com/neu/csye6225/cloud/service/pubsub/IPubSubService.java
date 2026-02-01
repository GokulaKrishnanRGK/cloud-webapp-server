package com.neu.csye6225.cloud.service.pubsub;

public interface IPubSubService {

  void publishMessage(String topicName, String message);
}
