package com.devhub.microservices.client;

import com.devhub.microservices.twitter_mock.model.Status;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class TwitterWebClient {
    private final WebClient webClient;

    public TwitterWebClient() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080/twitter-stream").build();
    }

    public Flux<Status> getTweets() {
        return webClient.get()
                .uri("/tweets")
                .retrieve()
                .bodyToFlux(Status.class);
    }

}
