package ru.cbrrate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import ru.cbrrate.model.GetUpdatesResponse;
import ru.cbrrate.services.BotException;
import ru.cbrrate.services.TelegramRequestStatisticsProcessor;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
public class KafkaConfig {

    public static final String TOPIC_RATE_REQUESTS = "RATE_REQUESTS";
    public static final String GROUP_ID = "RateRequestsProcessor";
    private final TelegramRequestStatisticsProcessor telegramRequestStatisticsProcessor;
    private final ObjectMapper objectMapper;

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(TOPIC_RATE_REQUESTS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_RATE_REQUESTS)
    public void rateRequestListen(String msgAsString) {
        GetUpdatesResponse.Message message;
        try {
            message = objectMapper.readValue(msgAsString, GetUpdatesResponse.Message.class);
        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new BotException("can't parse message:" + msgAsString, ex);
        }
        telegramRequestStatisticsProcessor.processMessage(message);
    }
}
