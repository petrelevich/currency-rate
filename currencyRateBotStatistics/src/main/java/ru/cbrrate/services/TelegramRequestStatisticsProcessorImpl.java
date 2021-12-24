package ru.cbrrate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cbrrate.model.GetUpdatesResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@Service
public class TelegramRequestStatisticsProcessorImpl implements TelegramRequestStatisticsProcessor {

    private final AtomicLong requestCounter = new AtomicLong(0);

    @Override
    public void processMessage(GetUpdatesResponse.Message message) {
        log.info("message:{}", message);
        var lastValue = requestCounter.incrementAndGet();
        log.info("current requestCounter:{}", lastValue);
    }

    @Override
    public long getRequestCounter() {
        return requestCounter.get();
    }
}
