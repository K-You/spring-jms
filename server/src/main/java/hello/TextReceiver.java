package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class TextReceiver {

    @Autowired
    ConfigurableApplicationContext context;

    @JmsListener(destination = "text-destination")
    @SendTo("other-destination")
    public String receiveMessage(String message) {
        System.out.println("Received message : " + message);
        return message;
    }

}