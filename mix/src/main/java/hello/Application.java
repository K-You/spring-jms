package hello;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

@SpringBootApplication
@EnableJms
public class Application {


    public static void main(String[] args) throws Exception{
    	

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
       
        
        Sender sender = context.getBean(Sender.class);
        sender.sendMessage("Hello World!");
        
        
        
    }

}