package ink.toppest.pushmq.config;


import ink.toppest.pushmq.api.MessageReceiver;
import ink.toppest.pushmq.api.PushClient;
import ink.toppest.pushmq.impl.DefaultMessageReceiver;
import ink.toppest.pushmq.impl.DefaultPushClient;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

@Configuration
public class MQConfig {

    @Value("${mq.push.group}")
    String pushGroup;

    @Value("${mq.push.broadcast}")
    String pushBroadcast;

    @ConditionalOnProperty(
            value = {"mq.producer"},
            matchIfMissing = false)
    @Bean
    Destination groupDestination(){
        return new ActiveMQQueue(pushGroup);
    }

    @ConditionalOnProperty(
            value = {"mq.producer"},
            matchIfMissing = false)
    @Bean
    Destination broadcastDestination(){
        return new ActiveMQQueue(pushBroadcast);
    }

    @ConditionalOnProperty(
            value = {"mq.producer"},
            matchIfMissing = false)
    @Bean
    PushClient pushClient(){
        return new DefaultPushClient();
    }

    @ConditionalOnProperty(
            value = {"mq.consumer"},
            matchIfMissing = false)
    @Bean
    MessageReceiver messageReceiver(){
        return new DefaultMessageReceiver();
    }


}

