package com.devhub.microservices.client;

import com.devhub.microservices.model.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;

@Component
public class TwitterWebClient {

    private final WebClient webClient;

    public TwitterWebClient(@NotNull @Value("${twitter.mock.url}") String twitterUrl) {
        this.webClient = WebClient.builder().baseUrl(twitterUrl).build();
    }

    public Flux<Status> getTweets() {
        return webClient.get()
                .uri("/tweets")
                .retrieve()
                .bodyToFlux(Status.class);
    }

}
