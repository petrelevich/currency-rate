package ru.cbrrate.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.cbrrate.config.CurrencyRateClientConfig;
import ru.cbrrate.model.CurrencyRate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateClientImpl implements CurrencyRateClient {
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CurrencyRateClientConfig config;

    private final HttpClientReactive httpClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<CurrencyRate> getCurrencyRate(String rateType, String currency, LocalDate date) {
        log.info("getCurrencyRate rateType:{}, currency:{}, date:{}", rateType, currency, date);
        var urlWithParams = String.format("%s/%s/%s/%s", config.getUrl(), rateType, currency, DATE_FORMATTER.format(date));

        try {
            return httpClient.performRequest(urlWithParams)
                    .map(this::parse);
        } catch (HttpClientException ex) {
            throw new CurrencyRateClientException("Error from Cbr Client host:" + ex.getMessage());
        } catch (Exception ex) {
            log.error("Getting currencyRate error, currency:{}, date:{}", currency, date, ex);
            throw new CurrencyRateClientException("Can't get currencyRate. currency:" + currency + ", date:" + date);
        }
    }

    private CurrencyRate parse(String rateAsString) {
        try {
            return objectMapper.readValue(rateAsString, CurrencyRate.class);
        } catch (Exception ex) {
            throw new CurrencyRateClientException("Can't parse string:" + rateAsString);
        }
    }

}
