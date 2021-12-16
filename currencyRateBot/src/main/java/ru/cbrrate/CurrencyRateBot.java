package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;



@SpringBootApplication
public class CurrencyRateBot {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRateBot.class).run(args);
    }
}
