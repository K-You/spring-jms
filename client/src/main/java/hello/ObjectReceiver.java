package hello;

import hello.entities.Pony;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ObjectReceiver {

    @Autowired
    ConfigurableApplicationContext context;

    @JmsListener(destination = "object-destination")
    public void receiveMessage(Pony poney) {
        System.out.println("Received object: " + poney);
    }

}