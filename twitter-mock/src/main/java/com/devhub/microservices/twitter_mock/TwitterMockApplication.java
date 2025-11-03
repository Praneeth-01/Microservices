package com.devhub.microservices.twitter_mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TwitterMockApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterMockApplication.class, args);
//		ApplicationContext appContext = SpringApplication.run(TwitterMockApplication.class, args);
//		Flux<Status> tweetsStream = appContext.getBean(TwitterStreamController.class).getTweets();
//		tweetsStream.subscribe();
	}

}
