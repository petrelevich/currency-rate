package ru.cbrrate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cbrrate.config.TelegramClientConfig;
import ru.cbrrate.model.SendMessageRequest;
import ru.cbrrate.services.TelegramException;


@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramClientImpl implements TelegramClient {

    private final HttpClient httpClientJdk;
    private final ObjectMapper objectMapper;
    private final TelegramClientConfig clientConfig;

    @Override
    public void sendMessage(SendMessageRequest request) {
        try {
            var params = objectMapper.writeValueAsString(request);
            log.info("params:{}", params);

            var responseAsString = httpClientJdk.performRequest(makeUrl(), params);
            log.info("responseAsString:{}", responseAsString);
        } catch (JsonProcessingException ex) {
            log.error("request:{}", request, ex);
            throw new TelegramException(ex);
        }
    }

    private String makeUrl() {
        return String.format("%s/bot%s/%s", clientConfig.getUrl(), clientConfig.getToken(), "sendMessage");
    }
}
