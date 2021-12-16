package ru.cbrrate;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;



@SpringBootApplication
public class CbrRate {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CbrRate.class).run(args);
    }
}
