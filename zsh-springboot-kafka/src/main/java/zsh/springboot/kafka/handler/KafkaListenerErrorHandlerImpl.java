package zsh.springboot.kafka.handler;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component("KafkaListenerErrorHandlerImpl")
public class KafkaListenerErrorHandlerImpl implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        if (message.getPayload() instanceof LinkedList) {
            LinkedList list = (LinkedList) message.getPayload();
            list.forEach(System.out::println);
        }
        return null;
    }
}
