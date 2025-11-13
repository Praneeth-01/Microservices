package com.devhub.microservices.service;

import com.devhub.microservices.client.TwitterWebClient;
import com.devhub.microservices.producer.TwitterKafkaProducer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwitterToKafkaServiceImpl {
    
    private static final Logger logger = LoggerFactory.getLogger(TwitterToKafkaServiceImpl.class);
    
    private final TwitterWebClient twitterWebClient;
    private final TwitterKafkaProducer twitterKafkaProducer;

    public TwitterToKafkaServiceImpl(TwitterWebClient twitterWebClient, 
                                     TwitterKafkaProducer twitterKafkaProducer) {
        this.twitterWebClient = twitterWebClient;
        this.twitterKafkaProducer = twitterKafkaProducer;
    }

    @PostConstruct
    public void init() {
        logger.info("Starting Twitter to Kafka streaming service...");
        twitterWebClient.getTweets()
                .subscribe(
                    tweet -> {
                        logger.info("Received tweet: {}", tweet);
                        twitterKafkaProducer.sendTweet(tweet);
                    },
                    error -> logger.error("Error receiving tweets: {}", error.getMessage(), error),
                    () -> logger.info("Twitter stream completed")
                );
    }
}
