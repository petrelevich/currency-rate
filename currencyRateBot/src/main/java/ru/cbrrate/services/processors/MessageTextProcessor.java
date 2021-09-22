package ru.cbrrate.services.processors;

import ru.cbrrate.model.MessageTextProcessorResult;

public interface MessageTextProcessor {
    MessageTextProcessorResult process(String msgText);
}
