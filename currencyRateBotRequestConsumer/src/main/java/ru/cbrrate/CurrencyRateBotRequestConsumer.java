package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;



@SpringBootApplication
public class CurrencyRateBotRequestConsumer {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRateBotRequestConsumer.class).run(args);
    }
}
