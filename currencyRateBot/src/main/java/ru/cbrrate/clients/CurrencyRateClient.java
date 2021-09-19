package ru.cbrrate.clients;

import ru.cbrrate.model.CurrencyRate;

import java.time.LocalDate;

public interface CurrencyRateClient {

    CurrencyRate getCurrencyRate(String rateType, String currency, LocalDate date);
}
