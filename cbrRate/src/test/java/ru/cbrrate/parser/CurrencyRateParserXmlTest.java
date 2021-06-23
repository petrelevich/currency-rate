package ru.cbrrate.parser;


import org.junit.jupiter.api.Test;
import ru.cbrrate.model.CurrencyRate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CurrencyRateParserXmlTest {

    @Test
    void parseTest() throws IOException, URISyntaxException {
        //given
        var parser = new CurrencyRateParserXml();
        var uri = ClassLoader.getSystemResource("cbr_response.xml").toURI();
        var ratesXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        //when
        var rates = parser.parse(ratesXml);

        //then
        assertThat(rates.size()).isEqualTo(34);
        assertThat(rates.contains(getUSDrate())).isTrue();
        assertThat(rates.contains(getEURrate())).isTrue();
        assertThat(rates.contains(getJPYrate())).isTrue();
    }

    CurrencyRate getUSDrate() {
        return CurrencyRate.builder()
                .numCode("840")
                .charCode("USD")
                .nominal("1")
                .name("Доллар США")
                .value("74,0448")
                .build();
    }

    CurrencyRate getEURrate() {
        return CurrencyRate.builder()
                .numCode("978")
                .charCode("EUR")
                .nominal("1")
                .name("Евро")
                .value("89,4461")
                .build();
    }

    CurrencyRate getJPYrate() {
        return CurrencyRate.builder()
                .numCode("392")
                .charCode("JPY")
                .nominal("100")
                .name("Японских иен")
                .value("69,4702")
                .build();
    }
}