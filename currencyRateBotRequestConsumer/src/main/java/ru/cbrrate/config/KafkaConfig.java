package ru.cbrrate.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.config.TopicBuilder;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

    public static final String TOPIC_RATE_REQUESTS = "RATE_REQUESTS";

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(TOPIC_RATE_REQUESTS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
