package ru.cbrrate.services;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.ContextConfiguration;
import ru.cbrrate.clients.TelegramClient;
import ru.cbrrate.model.GetUpdatesRequest;
import ru.cbrrate.model.GetUpdatesResponse;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static ru.cbrrate.config.ApplicationConfig.TELEGRAM_TOKEN_ENV_NAME;
import static ru.cbrrate.config.KafkaConfig.TOPIC_RATE_REQUESTS;

@SpringBootTest
@ContextConfiguration(initializers = {KafkaBase.Initializer.class})
class TelegramServiceImplTest {

    static {
        System.setProperty(TELEGRAM_TOKEN_ENV_NAME, "test");
    }

    @Autowired
    private MessageSender messageSender;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @MockBean
    private TelegramClient telegramClient;

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(TOPIC_RATE_REQUESTS, 1, (short) 1)));
    }

    @Test
    void getUpdatesTest() {
        var text = "text";
        var lastUpdateIdKeeper = spy(new LastUpdateIdKeeperImpl());
        var telegramService = new TelegramServiceImpl(telegramClient, lastUpdateIdKeeper, messageSender);

        //first run
        //given
        var request1 = new GetUpdatesRequest(0);
        var response1 = makeGetUpdatesResponse(1, text);
        when(telegramClient.getUpdates(request1)).thenReturn(response1);
        var expectedSentMessages = new ArrayList<>(response1.getResult().stream()
                .map(GetUpdatesResponse.Response::getMessage).toList());

        //when
        telegramService.getUpdates();

        //then
        verify(lastUpdateIdKeeper).set(response1.getResult().get(1).getUpdateId() + 1);

        //second run
        //given
        var request2 = new GetUpdatesRequest(response1.getResult().get(1).getUpdateId() + 1);
        var response2 = makeGetUpdatesResponse(2, text);
        when(telegramClient.getUpdates(request2)).thenReturn(response2);
        expectedSentMessages.addAll(response2.getResult().stream()
                .map(GetUpdatesResponse.Response::getMessage).toList());
        
        //when
        telegramService.getUpdates();

        verify(lastUpdateIdKeeper).set(response2.getResult().get(1).getUpdateId());

        var kafkaConsumer =  consumerFactory.createConsumer(TOPIC_RATE_REQUESTS, "clientId");
        kafkaConsumer.subscribe(List.of(TOPIC_RATE_REQUESTS));

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10_000));

        var factSentMessages = Streams.stream(records).map(this::parse).toList();
        assertThat(factSentMessages).hasSameElementsAs(expectedSentMessages);
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

    private GetUpdatesResponse.Message parse(ConsumerRecord<String, String> kafkaRecord) {
        try {
            return  objectMapper.readValue(kafkaRecord.value(), GetUpdatesResponse.Message.class);
        } catch (JacksonException ex) {
            throw new RuntimeException(ex);
        }
    }
}