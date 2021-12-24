package ru.cbrrate.services;

import org.junit.jupiter.api.Test;
import ru.cbrrate.clients.HttpClientJdk;
import ru.cbrrate.clients.TelegramClientImpl;
import ru.cbrrate.config.JsonConfig;
import ru.cbrrate.config.TelegramClientConfig;
import ru.cbrrate.model.SendMessageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TelegramClientTest {

    @Test
    void sendMessageTest() {
        //given
        var clientConfig = new TelegramClientConfig("https://api.telegram.org", "G23FD", 100);
        var request = new SendMessageRequest(11, "testOk", 173);

        var httpClientJdk = mock(HttpClientJdk.class);

        var objectMapper = new JsonConfig().objectMapper();
        var client = new TelegramClientImpl(httpClientJdk, objectMapper, clientConfig);

        //when
        client.sendMessage(request);

        //then
        var expectedUrl = String.format("%s/bot%s/sendMessage", clientConfig.getUrl(), clientConfig.getToken());
        var expectedParams = String.format("{\"chat_id\":%d,\"text\":\"%s\",\"reply_to_message_id\":%d}",
                request.getChatId(), request.getText(), request.getReplyToMessageId());

        verify(httpClientJdk).performRequest(expectedUrl, expectedParams);
    }
}
