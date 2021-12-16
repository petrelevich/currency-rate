package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;



@SpringBootApplication
public class CurrencyRateClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRateClient.class).run(args);
    }
}
