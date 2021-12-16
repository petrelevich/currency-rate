package ru.cbrrate.services;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.cbrrate.clients.CurrencyRateClient;
import ru.cbrrate.model.CurrencyRate;
import ru.cbrrate.model.MessageTextProcessorResult;
import ru.cbrrate.services.processors.MessageTextProcessorRate;
import ru.cbrrate.services.processors.Messages;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageTextProcessorRateTest {

    @Test
    void processTestAgrs3() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        var resultExpected = new MessageTextProcessorResult(currencyRate.getValue(), null);
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(Mono.just(currencyRate));

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "CBR USD 3-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        StepVerifier
                .create(result)
                .expectNext(resultExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void processTestAgrs2() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        var resultExpected = new MessageTextProcessorResult(currencyRate.getValue(), null);
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(Mono.just(currencyRate));

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD 03-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        StepVerifier
                .create(result)
                .expectNext(resultExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void processTestAgrs1() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        var resultExpected = new MessageTextProcessorResult(currencyRate.getValue(), null);
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(Mono.just(currencyRate));

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        StepVerifier
                .create(result)
                .expectNext(resultExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void processTestAgrsWrongData() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        var resultExpected = new MessageTextProcessorResult(null, Messages.DATA_FORMAT_MESSAGE.getText());
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(Mono.just(currencyRate));

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD 2021.03.01";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        StepVerifier
                .create(result)
                .expectNext(resultExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void processTestAgrsWrongFormat() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        var resultExpected = new MessageTextProcessorResult(null, Messages.EXPECTED_FORMAT_MESSAGE.getText());
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(Mono.just(currencyRate));

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "RRR RRR USD 03-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        StepVerifier
                .create(result)
                .expectNext(resultExpected)
                .expectComplete()
                .verify();
    }
}