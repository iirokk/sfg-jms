package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_NAME)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message) {
//        System.out.println("Message received!");
//        System.out.println(helloWorldMessage);
    }

    @JmsListener(destination = JmsConfig.SEND_AND_RECEIVE_QUEUE_NAME)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message) throws JMSException {
//        System.out.println("Message received!");
//        System.out.println(helloWorldMessage);

        HelloWorldMessage messagePayload = HelloWorldMessage.builder()
                .uuid(UUID.randomUUID())
                .message("World!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), messagePayload);
    }
}
