package ru.cbrrate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetUpdatesResponse {
    boolean ok;
    List<Response> result;

    @JsonCreator
    public GetUpdatesResponse(@JsonProperty("ok")boolean ok, @JsonProperty("result") List<Response> result) {
        this.ok = ok;
        this.result = result;
    }

    @Value
    public static class Response {
        long updateId;
        Message message;

        public Response(long updateId, Message message) {
            this.updateId = updateId;
            this.message = message;
        }
        @JsonCreator
        public Response(@JsonProperty("update_id") long updateId, @JsonProperty("message") Message message, @JsonProperty("edited_message") Message editedMessage) {
            this.updateId = updateId;
            this.message = message != null ? message : editedMessage;
        }
    }

    @Value
    public static class Message {
        long messageId;
        From from;
        Chat chat;
        long date;
        String text;

        @JsonCreator
        public Message(@JsonProperty("message_id") long messageId,
                       @JsonProperty("from") From from,
                       @JsonProperty("chat") Chat chat,
                       @JsonProperty("date") long date,
                       @JsonProperty("text") String text) {
            this.messageId = messageId;
            this.from = from;
            this.chat = chat;
            this.date = date;
            this.text = text;
        }
    }

    @Value
    public static class From {
        long id;
        boolean isBot;
        String firstName;
        String lastName;
        String languageCode;

        @JsonCreator
        public From(@JsonProperty("id") long id,
                    @JsonProperty("is_bot") boolean isBot,
                    @JsonProperty("first_name") String firstName,
                    @JsonProperty("last_name") String lastName,
                    @JsonProperty("language_code") String languageCode) {
            this.id = id;
            this.isBot = isBot;
            this.firstName = firstName;
            this.lastName = lastName;
            this.languageCode = languageCode;
        }
    }

    @Value
    public static class Chat {
        long id;
        String firstName;
        String lastName;
        String type;

        @JsonCreator
        public Chat(@JsonProperty("id") long id,
                    @JsonProperty("first_name") String firstName,
                    @JsonProperty("last_name") String lastName,
                    @JsonProperty("type") String type) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.type = type;
        }
    }
}
