package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class CurrencyRateBotStatistics {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRateBotStatistics.class).run(args);
    }
}
