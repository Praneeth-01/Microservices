package com.devhub.microservices.client;

import com.devhub.microservices.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class TwitterKafkaConsumerClient {
    @KafkaListener(topics = "twitter-tweets", groupId = "tweets-consumer")
    public void listen(String tweet) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Status status = objectMapper.readValue(tweet, Status.class);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
