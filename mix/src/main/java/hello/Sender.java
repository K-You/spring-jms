package hello;

import hello.entities.Pony;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String message) {
        jmsTemplate.send("text-destination", session -> session.createTextMessage(message));
        System.out.println("sending message: " + message);
    }


    public void sendMessage(Pony message) {
        jmsTemplate.send("object-destination", session -> session.createObjectMessage(message));
        System.out.println("sending message: " + message);
    }
}
