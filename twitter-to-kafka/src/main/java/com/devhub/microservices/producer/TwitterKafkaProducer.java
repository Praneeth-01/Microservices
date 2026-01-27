package com.devhub.microservices.producer;

import com.devhub.microservices.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TwitterKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    private final KafkaTemplate<String, Status> kafkaTemplate;

    @Value("${kafka.topic.twitter-tweets:twitter-tweets}")
    private String twitterTweetsTopic;

    public TwitterKafkaProducer(KafkaTemplate<String, Status> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTweet(Status tweet) {
        logger.info("Sending tweet to Kafka - ID: {}", tweet.getId());
        
        CompletableFuture<SendResult<String, Status>> future = 
            kafkaTemplate.send(twitterTweetsTopic, String.valueOf(tweet.getId()), tweet);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Successfully sent tweet to Kafka - ID: {}, Partition: {}, Offset: {}",
                        tweet.getId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send tweet to Kafka - ID: {}, Error: {}", 
                        tweet.getId(), ex.getMessage(), ex);
            }
        });
    }
}
