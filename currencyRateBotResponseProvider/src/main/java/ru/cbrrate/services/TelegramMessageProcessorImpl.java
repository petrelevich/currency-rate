package ru.cbrrate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.cbrrate.clients.TelegramClient;
import ru.cbrrate.model.GetUpdatesResponse;
import ru.cbrrate.model.SendMessageRequest;
import ru.cbrrate.services.processors.MessageTextProcessor;


@Slf4j
@Service
public class TelegramMessageProcessorImpl implements TelegramMessageProcessor {

    private final TelegramClient telegramClient;
    private final MessageTextProcessor messageTextProcessor;

    public TelegramMessageProcessorImpl(TelegramClient telegramClient,
                                        @Qualifier("messageTextProcessorGeneral")
                                        MessageTextProcessor messageTextProcessor) {
        this.telegramClient = telegramClient;
        this.messageTextProcessor = messageTextProcessor;
    }

    @Override
    public void processMessage(GetUpdatesResponse.Message message) {
        log.info("message:{}", message);

        var chatId = message.getChat().getId();
        var messageId = message.getMessageId();

        messageTextProcessor.process(message.getText())
                .doOnNext(result -> {
                            var replay = result.getFailReply() == null ? result.getOkReply() : result.getFailReply();
                            var sendMessageRequest = new SendMessageRequest(chatId, replay, messageId);
                            telegramClient.sendMessage(sendMessageRequest);
                        }
                ).subscribe();
    }
}
