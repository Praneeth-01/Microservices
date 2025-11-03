package com.devhub.microservices.twitter_mock.controller;

import com.devhub.microservices.twitter_mock.model.Status;
import com.devhub.microservices.twitter_mock.service.TwitterMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController("/twitter-stream")
public class TwitterStreamController {

    TwitterMockService twitterMockService;

    @Autowired
    public TwitterStreamController(TwitterMockService twitterMockService) {
        this.twitterMockService = twitterMockService;
    }

    @GetMapping("/tweets")
    public Flux<Status> getTweets() {
        return twitterMockService.getTweets();
    }
}
