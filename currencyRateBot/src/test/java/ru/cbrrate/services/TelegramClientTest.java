package ru.cbrrate.services;

import org.junit.jupiter.api.Test;
import ru.cbrrate.clients.HttpClientJdk;
import ru.cbrrate.clients.TelegramClientImpl;
import ru.cbrrate.config.JsonConfig;
import ru.cbrrate.config.TelegramClientConfig;
import ru.cbrrate.model.GetUpdatesRequest;
import ru.cbrrate.model.GetUpdatesResponse;
import ru.cbrrate.model.SendMessageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TelegramClientTest {

    @Test
    void getUpdatesTest() {
        //given
        var clientConfig = new TelegramClientConfig("https://api.telegram.org", "G23FD", 100);
        var request = new GetUpdatesRequest(11);

        var expectedUrl = String.format("%s/bot%s/getUpdates", clientConfig.getUrl(), clientConfig.getToken());
        var expectedParams = String.format("{\"offset\":%d}", request.getOffset());
        var httpClientJdk = mock(HttpClientJdk.class);
        when(httpClientJdk.performRequest(expectedUrl, expectedParams)).thenReturn(getResponseForUpdates());

        var objectMapper = new JsonConfig().objectMapper();
        var client = new TelegramClientImpl(httpClientJdk, objectMapper, clientConfig);


        //when
        var getUpdatesResponse = client.getUpdates(request);

        //then
        assertThat(getUpdatesResponse.getResult()).hasSize(2);

        var from1 = new GetUpdatesResponse.From(506L,   false, "Ivan" ,"Petrov","en");
        var chat1 = new GetUpdatesResponse.Chat(506L,"Ivan" ,"Petrov","private");
        var message1 = new GetUpdatesResponse.Message(3, from1, chat1,1631970287,"/help");
        var response1 = new GetUpdatesResponse.Response(953141213L, message1);

        var from2 = new GetUpdatesResponse.From(506L,   false, "Ivan" ,"Petrov","en");
        var chat2 = new GetUpdatesResponse.Chat(506L,"Ivan" ,"Petrov","private");
        var message2 = new GetUpdatesResponse.Message(4, from2, chat2,1631973072,"gggg");
        var response2 = new GetUpdatesResponse.Response(953141214L, message2);

        assertThat(getUpdatesResponse.getResult()).contains(response1, response2);
    }

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

    private String getResponseForUpdates() {
        return """
                 {"ok":true,"result":[{"update_id":953141213,
                         "message":{"message_id":3,"from":{"id":506,"is_bot":false,"first_name":"Ivan","last_name":"Petrov","language_code":"en"},"chat":{"id":506,"first_name":"Ivan","last_name":"Petrov","type":"private"},"date":1631970287,"text":"/help","entities":[{"offset":0,"length":5,"type":"bot_command"}]}},{"update_id":953141214,
                         "message":{"message_id":4,"from":{"id":506,"is_bot":false,"first_name":"Ivan","last_name":"Petrov","language_code":"en"},"chat":{"id":506,"first_name":"Ivan","last_name":"Petrov","type":"private"},"date":1631973072,"text":"gggg"}}]}
                """;
    }
}