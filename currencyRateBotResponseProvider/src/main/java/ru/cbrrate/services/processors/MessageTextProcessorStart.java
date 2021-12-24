package ru.cbrrate.services.processors;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.cbrrate.model.MessageTextProcessorResult;

import static ru.cbrrate.services.processors.Messages.EXPECTED_FORMAT_MESSAGE;

@Service("messageTextProcessorStart")
public class MessageTextProcessorStart implements MessageTextProcessor {
    @Override
    public Mono<MessageTextProcessorResult> process(String msgText) {
        return Mono.just(new MessageTextProcessorResult(EXPECTED_FORMAT_MESSAGE.getText(), null));
    }
}
