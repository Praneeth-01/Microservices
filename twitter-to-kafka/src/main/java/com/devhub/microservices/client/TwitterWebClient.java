package com.devhub.microservices.client;

import com.devhub.microservices.twitter_mock.model.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class TwitterWebClient {

    @Value("${twitter.mock.url}")
    private String twitterUrl;

    private final WebClient webClient;

    public TwitterWebClient() {
        if (twitterUrl == null) {
            throw new IllegalArgumentException("Twitter URL is not set");
        }
        this.webClient = WebClient.builder().baseUrl(twitterUrl).build();
    }

    public Flux<Status> getTweets() {
        return webClient.get()
                .uri("/tweets")
                .retrieve()
                .bodyToFlux(Status.class);
    }

}
