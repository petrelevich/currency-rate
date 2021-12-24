package ru.cbrrate.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.cbrrate.model.GetUpdatesResponse;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static ru.cbrrate.config.KafkaConfig.TOPIC_RATE_REQUESTS;

@SpringBootTest
@ContextConfiguration(initializers = {KafkaBase.Initializer.class})
class TelegramRequestStatisticsProcessorImplTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TelegramRequestStatisticsProcessor telegramRequestStatisticsProcessor;

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(TOPIC_RATE_REQUESTS, 1, (short) 1)));
    }

    @Test
    void processMessageTest()  {
        var text = "text";
        var responseList1 = makeGetUpdatesResponse(1, text).getResult().stream()
                .map(GetUpdatesResponse.Response::getMessage)
                .map(this::writeValueAsString).toList();

        var responseList2 = makeGetUpdatesResponse(2, text).getResult().stream()
                .map(GetUpdatesResponse.Response::getMessage)
                .map(this::writeValueAsString).toList();

        responseList1.forEach(this::sendSync);
        responseList2.forEach(this::sendSync);

        int responseCounter = responseList1.size() + responseList2.size();
        //first run

        //second run
        await().atMost(30, TimeUnit.SECONDS).until(() -> responseCounter == telegramRequestStatisticsProcessor.getRequestCounter());
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
