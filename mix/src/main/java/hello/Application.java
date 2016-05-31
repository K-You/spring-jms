package hello;

import hello.entities.Pony;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class Application {


    public static void main(String[] args) throws Exception {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        Sender sender = context.getBean(Sender.class);
        sender.sendMessage("Coucou toi!");
        sender.sendMessage(new Pony("Little pony"));
    }

}