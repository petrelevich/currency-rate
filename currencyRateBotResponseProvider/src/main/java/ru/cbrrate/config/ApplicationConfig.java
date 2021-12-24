package ru.cbrrate.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cbrrate.services.TelegramException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(CurrencyRateClientConfig.class)
public class ApplicationConfig {
    public static final String TELEGRAM_TOKEN_ENV_NAME = "TELEGRAM_TOKEN";
    public static final String TOKEN_FILE = "TOKEN_FILE";

    @Bean
    public TelegramClientConfig telegramClientConfig(@Value("${app.telegram.url}") String url,
                                                     @Value("${app.telegram.refresh-rate-ms}") int refreshRateMs) {
        var token = System.getProperty(TELEGRAM_TOKEN_ENV_NAME);
        if (token == null) {
            token = System.getenv(TELEGRAM_TOKEN_ENV_NAME);
        }
        if (token == null) {
            var tokenFile = System.getenv(TOKEN_FILE);
            token = readFile(tokenFile);
        }
        if (token == null) {
            log.error("telegram token not found");
            throw new TelegramException("telegram token not found");
        }
        return new TelegramClientConfig(url, token, refreshRateMs);
    }

    private String readFile(String tokenFile) {
        try {
            if (tokenFile != null) {
                return Files.readString(Path.of(tokenFile));
            }
            return null;
        } catch (IOException e) {
            throw new TelegramException("can't read file:" + tokenFile);
        }
    }
}
