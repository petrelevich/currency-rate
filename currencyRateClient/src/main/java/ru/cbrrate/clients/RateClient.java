package ru.cbrrate.clients;

import reactor.core.publisher.Mono;
import ru.cbrrate.model.CurrencyRate;

import java.time.LocalDate;

public interface RateClient {

    Mono<CurrencyRate> getCurrencyRate(String currency, LocalDate date);
}
