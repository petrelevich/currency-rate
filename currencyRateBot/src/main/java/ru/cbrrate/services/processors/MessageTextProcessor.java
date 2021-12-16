package ru.cbrrate.services.processors;

import reactor.core.publisher.Mono;
import ru.cbrrate.model.MessageTextProcessorResult;

public interface MessageTextProcessor {
    Mono<MessageTextProcessorResult> process(String msgText);
}
