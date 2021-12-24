package ru.cbrrate.clients;

import ru.cbrrate.model.GetUpdatesRequest;
import ru.cbrrate.model.GetUpdatesResponse;

public interface TelegramClient {

    GetUpdatesResponse getUpdates(GetUpdatesRequest request);
}
