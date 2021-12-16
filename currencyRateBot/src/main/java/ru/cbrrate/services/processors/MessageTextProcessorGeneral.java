package ru.cbrrate.services.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.cbrrate.model.MessageTextProcessorResult;


@Slf4j
@Service("messageTextProcessorGeneral")
public class MessageTextProcessorGeneral implements MessageTextProcessor {

    private final ApplicationContext applicationContext;
    private final MessageTextProcessor messageTextProcessorRate;

    public MessageTextProcessorGeneral(ApplicationContext applicationContext,
                                       @Qualifier("messageTextProcessorRate") MessageTextProcessor messageTextProcessor) {
        this.applicationContext = applicationContext;
        this.messageTextProcessorRate = messageTextProcessor;
    }

    @Override
    public Mono<MessageTextProcessorResult> process(String msgText) {
        for(var cmd : CmdRegistry.values()) {
            if (cmd.getCmd().equals(msgText)) {
                var handler = applicationContext.getBean(cmd.getHandlerName(), MessageTextProcessor.class);
                return handler.process(msgText);
            }
        }
        return messageTextProcessorRate.process(msgText);
    }
}
