package ru.cbrrate.services;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ru.cbrrate.clients.TelegramClient;
import ru.cbrrate.model.GetUpdatesRequest;
import ru.cbrrate.model.GetUpdatesResponse;
import ru.cbrrate.model.SendMessageRequest;
import ru.cbrrate.services.processors.MessageTextProcessor;
import ru.cbrrate.model.MessageTextProcessorResult;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

class TelegramServiceImplTest {

    @Test
    void getUpdatesTest() {
        var telegramClient = mock(TelegramClient.class);
        var text = "text";
        var messageTextProcessor = mock(MessageTextProcessor.class);
        var reply = "Ok";
        when(messageTextProcessor.process(text)).thenReturn(Mono.just(new MessageTextProcessorResult(reply, null)));

        var lastUpdateIdKeeper = spy(new LastUpdateIdKeeperImpl());
        var telegramService = new TelegramServiceImpl(telegramClient, messageTextProcessor, lastUpdateIdKeeper);

        //first run
        //given
        var request1 = new GetUpdatesRequest(0);
        var response1 = makeGetUpdatesResponse(1, text);
        when(telegramClient.getUpdates(request1)).thenReturn(response1);

        //when
        telegramService.getUpdates();

        //then
        var sendMessageRequest1 = new SendMessageRequest(response1.getResult().get(0).getMessage().getChat().getId(),
                reply, response1.getResult().get(0).getMessage().getMessageId());

        var sendMessageRequest2 = new SendMessageRequest(response1.getResult().get(1).getMessage().getChat().getId(),
                reply, response1.getResult().get(1).getMessage().getMessageId());

        verify(telegramClient).sendMessage(sendMessageRequest1);
        verify(telegramClient).sendMessage(sendMessageRequest2);
        verify(lastUpdateIdKeeper).set(response1.getResult().get(1).getUpdateId() + 1);

        //second run
        //given
        var request2 = new GetUpdatesRequest(response1.getResult().get(1).getUpdateId() + 1);
        var response2 = makeGetUpdatesResponse(2, text);
        when(telegramClient.getUpdates(request2)).thenReturn(response2);

        //when
        telegramService.getUpdates();

        var sendMessageRequest3 = new SendMessageRequest(response2.getResult().get(0).getMessage().getChat().getId(),
                reply, response2.getResult().get(0).getMessage().getMessageId());

        var sendMessageRequest4 = new SendMessageRequest(response2.getResult().get(1).getMessage().getChat().getId(),
                reply, response2.getResult().get(1).getMessage().getMessageId());

        verify(telegramClient).sendMessage(sendMessageRequest3);
        verify(telegramClient).sendMessage(sendMessageRequest4);
        verify(lastUpdateIdKeeper).set(response2.getResult().get(1).getUpdateId());
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