package ru.cbrrate.services;

import ru.cbrrate.model.GetUpdatesResponse;

public interface MessageSender {
    void send(GetUpdatesResponse.Message message);
}
