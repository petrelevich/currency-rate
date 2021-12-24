package ru.cbrrate.services;

import lombok.extern.slf4j.Slf4j;
import ru.cbrrate.clients.TelegramClient;
import ru.cbrrate.model.GetUpdatesRequest;
import ru.cbrrate.model.GetUpdatesResponse;


@Slf4j
public class TelegramServiceImpl implements TelegramService {

    private final TelegramClient telegramClient;
    private final LastUpdateIdKeeper lastUpdateIdKeeper;
    private final MessageSender messageSender;

    public TelegramServiceImpl(TelegramClient telegramClient,
                               LastUpdateIdKeeper lastUpdateIdKeeper,
                               MessageSender messageSender) {
        this.telegramClient = telegramClient;
        this.lastUpdateIdKeeper = lastUpdateIdKeeper;
        this.messageSender = messageSender;
    }

    @Override
    public void getUpdates() {
        try {
            log.info("getUpdates begin");
            var offset = lastUpdateIdKeeper.get();
            var request = new GetUpdatesRequest(offset);
            var response = telegramClient.getUpdates(request);
            var lastUpdateId = processResponse(response);
            lastUpdateId = lastUpdateId == 0 ? offset : lastUpdateId + 1;
            lastUpdateIdKeeper.set(lastUpdateId);
            log.info("getUpdates end, lastUpdateId:{}", lastUpdateId);
        } catch (Exception ex) {
            log.error("unhandled exception", ex);
        }
    }

    private long processResponse(GetUpdatesResponse response) {
        log.info("response.getResult().size:{}", response.getResult().size());
        long lastUpdateId = 0;
        for (var responseMsg : response.getResult()) {
            lastUpdateId = Math.max(lastUpdateId, responseMsg.getUpdateId());
            messageSender.send(responseMsg.getMessage());
        }
        log.info("lastUpdateId:{}", lastUpdateId);
        return lastUpdateId;
    }
}
