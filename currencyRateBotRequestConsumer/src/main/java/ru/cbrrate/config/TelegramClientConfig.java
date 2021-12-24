package ru.cbrrate.config;

import lombok.Value;

@Value
public class TelegramClientConfig {
    String url;
    String token;
    int refreshRateMs;
}
