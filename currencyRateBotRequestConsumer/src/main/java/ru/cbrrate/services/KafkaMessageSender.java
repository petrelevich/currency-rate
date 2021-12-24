package ru.cbrrate.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.cbrrate.model.GetUpdatesResponse;

import static ru.cbrrate.config.KafkaConfig.TOPIC_RATE_REQUESTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSender implements MessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(GetUpdatesResponse.Message message) {
        log.info("send message:{}", message);
        String messageAsString;
        try {
            messageAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new BotException("can't send message:" + message, ex);
        }
        kafkaTemplate.send(TOPIC_RATE_REQUESTS, messageAsString);
    }
}
