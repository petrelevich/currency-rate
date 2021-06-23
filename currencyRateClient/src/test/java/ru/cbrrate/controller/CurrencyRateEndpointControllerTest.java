package ru.cbrrate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.cbrrate.clients.CbrRateClient;
import ru.cbrrate.clients.HttpClient;
import ru.cbrrate.config.ApplicationConfig;
import ru.cbrrate.config.CbrRateClientConfig;
import ru.cbrrate.config.JsonConfig;
import ru.cbrrate.services.CurrencyRateEndpointService;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyRateEndpointController.class)
@Import({ApplicationConfig.class, JsonConfig.class, CurrencyRateEndpointService.class, CbrRateClient.class})
class CurrencyRateEndpointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CbrRateClientConfig config;

    @MockBean
    HttpClient httpClient;

    @Test
    void getCurrencyRateTest() throws Exception {
        //given
        var type = "CBR";
        var currency = "EUR";
        var date = "02-03-2021";

        var url = String.format("%s/%s/%s", config.getUrl(), currency, date);
        when(httpClient.performRequest(url))
                .thenReturn("{\"numCode\":\"978\",\"charCode\":\"EUR\",\"nominal\":\"1\",\"name\":\"Евро\",\"value\":\"89,4461\"}");
        //when
        var result = mockMvc.perform(get(String.format("/api/v1/currencyRate/%s/%s/%s", type, currency, date)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(result).isEqualTo("{\"charCode\":\"EUR\",\"nominal\":\"1\",\"value\":\"89,4461\"}");
    }
}