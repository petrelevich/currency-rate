package ru.cbrrate.clients;

import ru.cbrrate.model.CurrencyRate;

import java.time.LocalDate;

public interface RateClient {

    CurrencyRate getCurrencyRate(String currency, LocalDate date);
}
