package hello;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import hello.entities.Pony;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.apache.activemq.usage.SystemUsage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import java.util.Collections;

@Configuration
@ComponentScan(value = {"hello"})
public class JmsConfig {

    public static final String QUEUE_NAME = "MSG_QUEUE_NAME";
    private static final int LIMIT_IN_MB = 1024 * 1024 * 32;

    //The broker url 
    @Value("${broker.url:tcp://localhost:61616}")
    private String brokerUrl;

    //check env vars
    @Value("${isServer:false}")
    private boolean isServer;

    @Bean
    ConnectionFactory connectionFactory() throws Exception {
        if (isServer) {
            configureAndStartBroker();
        }
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setTrustedPackages(Collections.singletonList(Pony.class.getPackage().getName()));
        factory.setBrokerURL(brokerUrl);
        return factory;
    }

    private void configureAndStartBroker() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.addConnector(brokerUrl);

        //??
        brokerService.setUseShutdownHook(false);

        SystemUsage systemUsage = brokerService.getSystemUsage();
        systemUsage.getStoreUsage().setLimit(LIMIT_IN_MB);
        systemUsage.getTempUsage().setLimit(LIMIT_IN_MB);

        brokerService.setPersistent(false);

        MemoryPersistenceAdapter memoryPersistenceAdapter = new MemoryPersistenceAdapter();
        brokerService.setPersistenceAdapter(memoryPersistenceAdapter);

        brokerService.start();
    }

    @Bean
    ConnectionFactory cachedConnectionFactory() throws Exception {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactory());
        cachingConnectionFactory.setSessionCacheSize(10);
        return cachingConnectionFactory;
    }

    @Bean
    Destination destinationQueue() {
        return new ActiveMQQueue(QUEUE_NAME);
    }

    @Bean
    JmsTemplate jmsTemplate() throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachedConnectionFactory());
        jmsTemplate.setDefaultDestination(destinationQueue());
        return jmsTemplate;
    }

    @Bean
    MessageListenerAdapter adapter(TextReceiver textReceiver) {
        return new MessageListenerAdapter(textReceiver) {
            {
                setDefaultListenerMethod("receiveMessage");
            }
        };
    }

    @Bean
    public SimpleMessageListenerContainer container(final MessageListenerAdapter messageListener, final ConnectionFactory cachedConnectionFactory) {
        return new SimpleMessageListenerContainer() {
            {
                setMessageListener(messageListener);
                setConnectionFactory(cachedConnectionFactory);
                setDestination(destinationQueue());
                setPubSubDomain(true);
            }
        };
    }
}
