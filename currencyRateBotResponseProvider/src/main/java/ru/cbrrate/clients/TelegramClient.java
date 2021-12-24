package ru.cbrrate.clients;

import ru.cbrrate.model.SendMessageRequest;


public interface TelegramClient {

    void sendMessage(SendMessageRequest request);
}
