package ru.cbrrate.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import ru.cbrrate.clients.TelegramClient;
import ru.cbrrate.model.GetUpdatesResponse;
import ru.cbrrate.model.SendMessageRequest;

import ru.cbrrate.model.MessageTextProcessorResult;
import ru.cbrrate.services.processors.MessageTextProcessorRate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.cbrrate.config.ApplicationConfig.TELEGRAM_TOKEN_ENV_NAME;
import static ru.cbrrate.config.KafkaConfig.TOPIC_RATE_REQUESTS;

@SpringBootTest
@ContextConfiguration(initializers = {KafkaBase.Initializer.class})
class TelegramMessageProcessorImplTest {

    static {
        System.setProperty(TELEGRAM_TOKEN_ENV_NAME, "test");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private MessageTextProcessorRate messageTextProcessorRate;

    @MockBean
    private TelegramClient telegramClient;

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(TOPIC_RATE_REQUESTS, 1, (short) 1)));
    }

    @Test
    void processMessageTest()  {
        int timeoutMs = 1_000;
        var text = "text";
        var reply = "Ok";
        when(messageTextProcessorRate.process(text)).thenReturn(Mono.just(new MessageTextProcessorResult(reply, null)));

        var response1 = makeGetUpdatesResponse(1, text);
        putResponseToKafka(response1);
        var response2 = makeGetUpdatesResponse(2, text);
        putResponseToKafka(response2);

        //first run
        var sendMessageRequest1 = new SendMessageRequest(response1.getResult().get(0).getMessage().getChat().getId(),
                reply, response1.getResult().get(0).getMessage().getMessageId());

        var sendMessageRequest2 = new SendMessageRequest(response1.getResult().get(1).getMessage().getChat().getId(),
                reply, response1.getResult().get(1).getMessage().getMessageId());

        verify(telegramClient, timeout(timeoutMs)).sendMessage(sendMessageRequest1);
        verify(telegramClient, timeout(timeoutMs)).sendMessage(sendMessageRequest2);

        //second run
        var sendMessageRequest3 = new SendMessageRequest(response2.getResult().get(0).getMessage().getChat().getId(),
                reply, response2.getResult().get(0).getMessage().getMessageId());

        var sendMessageRequest4 = new SendMessageRequest(response2.getResult().get(1).getMessage().getChat().getId(),
                reply, response2.getResult().get(1).getMessage().getMessageId());

        verify(telegramClient, timeout(timeoutMs)).sendMessage(sendMessageRequest3);
        verify(telegramClient, timeout(timeoutMs)).sendMessage(sendMessageRequest4);
    }

    private void putResponseToKafka(GetUpdatesResponse response) {
        response.getResult().stream()
                .map(GetUpdatesResponse.Response::getMessage)
                .map(this::writeValueAsString)
                .forEach(this::sendSync);
    }

    private void sendSync(String val) {
        try {
            kafkaTemplate.send(TOPIC_RATE_REQUESTS, val);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String writeValueAsString(GetUpdatesResponse.Message msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private GetUpdatesResponse makeGetUpdatesResponse(long updateId, String text) {
        var from = new GetUpdatesResponse.From(506L, false, "Ivan", "Petrov", "en");
        var chat = new GetUpdatesResponse.Chat(506L, "Ivan", "Petrov", "private");
        var random = new Random();
        var message1 = new GetUpdatesResponse.Message(random.nextLong(), from, chat, 1631970287, text);
        var message2 = new GetUpdatesResponse.Message(random.nextLong(), from, chat, 1631970287, text);

        return new GetUpdatesResponse(true, List.of(new GetUpdatesResponse.Response(updateId, message1),
                new GetUpdatesResponse.Response(updateId + 1, message2)));
    }
}
