package ink.toppest.pushclient.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

@Configuration
public class MQConfig {

    @Value("${mq.push.group}")
    String pushGroup;

    @Value("${mq.push.broadcast}")
    String pushBroadcast;

    @Bean
    Destination groupDestination(){
        return new ActiveMQQueue(pushGroup);
    }

    @Bean
    Destination broadcastDestination(){
        return new ActiveMQQueue(pushBroadcast);
    }
}
