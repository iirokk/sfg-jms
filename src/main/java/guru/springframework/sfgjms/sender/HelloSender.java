package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .uuid(UUID.randomUUID())
                .message("Hello World!")
                .build();
        jmsTemplate.convertAndSend(JmsConfig.QUEUE_NAME, message);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .uuid(UUID.randomUUID())
                .message("Hello!")
                .build();
        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.SEND_AND_RECEIVE_QUEUE_NAME, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message helloMessage;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                    System.out.println("Sending Hello");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("JMS sendAndReceiveMessage failed.");
                }
            }
        });
        System.out.println(receivedMessage.getBody(String.class));
    }
}
