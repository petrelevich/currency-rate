package ru.cbrrate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.cbrrate.clients.HttpClient;
import ru.cbrrate.config.CbrRateClientConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyRateEndpointControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    CbrRateClientConfig config;

    @MockBean
    HttpClient httpClient;

    @Test
    void getCurrencyRateTest()  {
        //given
        var type = "CBR";
        var currency = "EUR";
        var date = "02-03-2021";

        var url = String.format("%s/%s/%s", config.getUrl(), currency, date);
        when(httpClient.performRequest(url))
                .thenReturn(Mono.just("{\"numCode\":\"978\",\"charCode\":\"EUR\",\"nominal\":\"1\",\"name\":\"Евро\",\"value\":\"89,4461\"}"));
        //when

        var result = webTestClient
                .get().uri(String.format("/api/v1/currencyRate/%s/%s/%s", type, currency, date))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockLast();

        //then
        assertThat(result).isEqualTo("{\"charCode\":\"EUR\",\"nominal\":\"1\",\"value\":\"89,4461\"}");
    }
}