package ru.cbrrate.services;

import ru.cbrrate.model.GetUpdatesResponse;

public interface TelegramRequestStatisticsProcessor {
    void processMessage(GetUpdatesResponse.Message message);

    long getRequestCounter();
}
