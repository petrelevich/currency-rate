package ru.cbrrate.services;

import ru.cbrrate.model.GetUpdatesResponse;

public interface TelegramMessageProcessor {
    void processMessage(GetUpdatesResponse.Message message);
}
