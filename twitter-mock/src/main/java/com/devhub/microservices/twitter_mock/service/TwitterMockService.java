package com.devhub.microservices.twitter_mock.service;

import com.devhub.microservices.twitter_mock.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Service
public class TwitterMockService {

    private final ChatClient chatClient;
    public final String PROMPT = """
                Generate a random tweet about pets in text format.
                You can pick any animal.
                Make it funny or surprising or sad or angry, any emotion.
                Do NOT repeat older ideas.
                Just the tweet text.
            """;

    @Autowired
    public TwitterMockService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<Status> getTweets() {

        return Flux.interval(Duration.ofSeconds(5))
                .delayElements(Duration.ofSeconds(getRandomNumberInRange(0, 5)))
                .flatMap(_ -> getRandomTweet())
                .map(responseText -> {
                    Status tweet = Status.builder()
                            .id(RandomGenerator.getDefault().nextLong())
                            .createdAt(new Date())
                            .userId(getRandomNumberInRange(1, 1000))
                            .text(responseText)
                            .build();
                    log.info("Generated tweet: {}", tweet);
                    return tweet;
                });
    }

    private Mono<String> getRandomTweet() {
        return Mono.fromCallable(() -> chatClient.prompt(PROMPT + "\n\nseed :" + UUID.randomUUID())
                        .options(ChatOptions.builder().temperature(1.3).build())
                        .call()
                        .content()
                ).subscribeOn(Schedulers.boundedElastic());
    }

    private int getRandomNumberInRange(int min, int max) {
        return RandomGenerator.getDefault().nextInt(min, max + 1);
    }
}
