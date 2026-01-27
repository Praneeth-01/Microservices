package com.devhub.microservices.config;

import com.devhub.microservices.model.Status;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        // JSON Serializer specific settings
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        
        // Producer reliability settings
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas to acknowledge
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // Retry up to 3 times
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // Wait 1 second between retries
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Ensure exactly-once semantics
        
        // Performance settings
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // Compress messages
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // Batch size in bytes
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // Wait up to 10ms to batch messages
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32MB buffer
        
        return props;
    }

    @Bean
    public ProducerFactory<String, Status> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Status> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
