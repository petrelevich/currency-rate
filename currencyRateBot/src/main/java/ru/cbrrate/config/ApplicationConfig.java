package ru.cbrrate.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cbrrate.services.*;
import ru.cbrrate.services.processors.MessageTextProcessor;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(CurrencyRateClientConfig.class)
public class ApplicationConfig {
    public static final String TELEGRAM_TOKEN_ENV_NAME = "TELEGRAM_TOKEN";

    @Bean
    public TelegramClientConfig telegramClientConfig(@Value("${app.telegram.url}") String url,
                                                     @Value("${app.telegram.refresh-rate-ms}") int refreshRateMs) {
        String token = System.getProperty(TELEGRAM_TOKEN_ENV_NAME);
        if (token == null) {
            log.error("telegram token not found");
            throw new TelegramException("telegram token not found");
        }
        return new TelegramClientConfig(url, token, refreshRateMs);
    }

    @Bean
    public TelegramImporterScheduled telegramImporterScheduled(TelegramClient telegramClient,
                                                               TelegramClientConfig telegramClientConfig,
                                                               @Qualifier("messageTextProcessorGeneral") MessageTextProcessor processorGeneral,
                                                               LastUpdateIdKeeper lastUpdateIdKeeper) {
        var telegramService = new TelegramServiceImpl(telegramClient, processorGeneral, lastUpdateIdKeeper);
        return new TelegramImporterScheduled(telegramService, telegramClientConfig);
    }

    public static class TelegramImporterScheduled {
        public TelegramImporterScheduled(TelegramService telegramService, TelegramClientConfig telegramClientConfig) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(telegramService::getUpdates, 0, telegramClientConfig.getRefreshRateMs(), TimeUnit.MILLISECONDS);
        }
    }
}
