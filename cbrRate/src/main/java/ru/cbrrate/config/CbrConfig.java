package ru.cbrrate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cbr")
public class CbrConfig {
    String url;
}
