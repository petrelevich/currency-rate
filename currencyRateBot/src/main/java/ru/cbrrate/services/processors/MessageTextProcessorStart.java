package ru.cbrrate.services.processors;


import org.springframework.stereotype.Service;
import ru.cbrrate.model.MessageTextProcessorResult;

import static ru.cbrrate.services.processors.Messages.EXPECTED_FORMAT_MESSAGE;

@Service("messageTextProcessorStart")
public class MessageTextProcessorStart implements MessageTextProcessor {
    @Override
    public MessageTextProcessorResult process(String msgText) {
        return new MessageTextProcessorResult(EXPECTED_FORMAT_MESSAGE.getText(), null);
    }
}
