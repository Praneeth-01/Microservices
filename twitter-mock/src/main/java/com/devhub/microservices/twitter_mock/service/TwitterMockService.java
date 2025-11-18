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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class TwitterMockService {

    private final ChatClient chatClient;
    List<String> topics = List.of("pets", "sports", "technology", "music", "travel", "food", "movies", "books", "fitness", "gaming");
    List<String> tones = List.of("funny", "serious", "sarcastic", "optimistic", "pessimistic", "enthusiastic", "informative", "casual", "formal", "playful");
    public final String PROMPT = """
                write one short tweet about %s.
                tone should feel %s.
                use natural casual human language.
                do not announce that you are writing a tweet.
                do not start with the word Just.
            """;

    @Autowired
    public TwitterMockService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<Status> getTweets() {

        return Flux.interval(Duration.ofSeconds(5))
                .onBackpressureBuffer()
                .flatMap(t -> getRandomTweet())
                .map(responseText -> {
                    Status tweet = Status.builder()
                            .id(ThreadLocalRandom.current().nextLong())
                            .createdAt(new Date())
                            .userId(getRandomNumberInRange(1, 1000))
                            .text(responseText)
                            .build();
                    log.info("Generated tweet: {}", tweet);
                    return tweet;
                });
    }

    private Mono<String> getRandomTweet() {
        String topic = topics.get(getRandomNumberInRange(0, topics.size() - 1));
        String tone = tones.get(getRandomNumberInRange(0, tones.size() - 1));
        return Mono.fromCallable(() -> chatClient.prompt(PROMPT.formatted(topic, tone) + "\n\nseed :" + UUID.randomUUID())
                        .options(ChatOptions.builder().temperature(1.3).build())
                        .call()
                        .content()
                ).subscribeOn(Schedulers.boundedElastic());
    }

    private int getRandomNumberInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
