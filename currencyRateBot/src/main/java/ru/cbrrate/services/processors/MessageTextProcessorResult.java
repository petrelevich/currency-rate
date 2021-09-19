package ru.cbrrate.services.processors;

import lombok.Value;

@Value
public class MessageTextProcessorResult {
    String okReply;
    String failReply;
}
