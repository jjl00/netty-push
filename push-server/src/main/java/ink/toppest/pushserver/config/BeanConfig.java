package ink.toppest.pushserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import protocol.MessageProtocol;

import java.util.concurrent.*;

@Configuration
public class BeanConfig {
    @Bean
    MessageProtocol.Protocol authenticated(){
        return MessageProtocol.Protocol.newBuilder()
                .setType(MessageProtocol.Protocol.headType.AUTHENTICATED)
                .build();
    }

    @Bean
    ExecutorService singleThreadExecutor(){
        return Executors.newSingleThreadExecutor();
    }
}
