package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class CurrencyRateBotResponseProvider {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRateBotResponseProvider.class).run(args);
    }
}
