package com.devhub.microservices.controller;

import com.devhub.microservices.model.Status;
import com.devhub.microservices.service.TwitterMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/twitter-stream")
public class TwitterStreamController {

    TwitterMockService twitterMockService;

    @Autowired
    public TwitterStreamController(TwitterMockService twitterMockService) {
        this.twitterMockService = twitterMockService;
    }

    @GetMapping(value="/tweets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Status> getTweets() {
        return twitterMockService.getTweets();
    }
}
