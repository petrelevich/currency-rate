package ru.cbrrate.services;

import org.junit.jupiter.api.Test;
import ru.cbrrate.clients.CurrencyRateClient;
import ru.cbrrate.model.CurrencyRate;
import ru.cbrrate.services.processors.MessageTextProcessorRate;
import ru.cbrrate.services.processors.Messages;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageTextProcessorRateTest {

    @Test
    void processTestAgrs3() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(currencyRate);

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "CBR USD 3-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        assertThat(result.getFailReply()).isNull();
        assertThat(result.getOkReply()).isEqualTo(currencyRate.getValue());
    }

    @Test
    void processTestAgrs2() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(currencyRate);

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD 03-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        assertThat(result.getFailReply()).isNull();
        assertThat(result.getOkReply()).isEqualTo(currencyRate.getValue());
    }

    @Test
    void processTestAgrs1() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(currencyRate);

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        assertThat(result.getFailReply()).isNull();
        assertThat(result.getOkReply()).isEqualTo(currencyRate.getValue());
    }

    @Test
    void processTestAgrsWrongData() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(currencyRate);

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "USD 2021.03.01";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        assertThat(result.getOkReply()).isNull();
        assertThat(result.getFailReply()).isEqualTo(Messages.DATA_FORMAT_MESSAGE.getText());
    }

    @Test
    void processTestAgrsWrongFormat() {
        //given
        var currencyRateClient = mock(CurrencyRateClient.class);
        var currencyRate = new CurrencyRate("USD", "1", "49.4");
        when(currencyRateClient.getCurrencyRate("CBR", "USD", LocalDate.of(2021, 2, 3)))
                .thenReturn(currencyRate);

        var messageTextProcessor = new MessageTextProcessorRate(currencyRateClient,
                () -> LocalDateTime.of(2021, 2, 3,1,1,1));
        var msg = "RRR RRR USD 03-02-2021";

        //when
        var result = messageTextProcessor.process(msg);

        //then
        assertThat(result.getOkReply()).isNull();
        assertThat(result.getFailReply()).isEqualTo(Messages.EXPECTED_FORMAT_MESSAGE.getText());
    }

}