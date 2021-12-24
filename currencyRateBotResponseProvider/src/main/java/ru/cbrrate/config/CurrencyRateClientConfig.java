package ru.cbrrate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "currency-rate-client")
public class CurrencyRateClientConfig {
    String url;
}
