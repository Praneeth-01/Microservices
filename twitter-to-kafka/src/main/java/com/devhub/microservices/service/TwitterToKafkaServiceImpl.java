package com.devhub.microservices.service;

import com.devhub.microservices.client.TwitterWebClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class TwitterToKafkaServiceImpl {
    private final TwitterWebClient twitterWebClient;

    public TwitterToKafkaServiceImpl(TwitterWebClient twitterWebClient) {
        this.twitterWebClient = twitterWebClient;
    }

    @PostConstruct
    public void init() {
        twitterWebClient.getTweets()
                .subscribe(tweet -> {
                    // Here you would normally send the tweet to Kafka
                    System.out.println("Received tweet: " + tweet.toString());
                });
    }
}
