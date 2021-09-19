package ru.cbrrate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SendMessageRequest {

    @JsonProperty("chat_id")
    long chatId;

    @JsonProperty("text")
    String text;

    @JsonProperty("reply_to_message_id")
    long replyToMessageId;
}
