package com.neu.csye6225.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Profile("aws")
@Configuration
public class AwsSnsConfig {

  @Bean
  public SnsClient snsClient(@Value("${aws.region:us-east-1}") String region) {
    return SnsClient.builder()
        .region(Region.of(region))
        .build();
  }
}
