package ink.toppest.pushclient.config;


import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import protocol.MessageProtocol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {

    @Value("${clientId}")
    long clientId;

    @Bean
    ScheduledExecutorService singleThreadScheduledExecutor(){
        return Executors.newSingleThreadScheduledExecutor();
    }


    @Bean
    public MessageProtocol.Protocol heartBeat(){
        MessageProtocol.Protocol protocol=MessageProtocol.Protocol.newBuilder()
                .setMessageId(clientId).setType(MessageProtocol.Protocol.headType.PING)
                .build();
        return protocol;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }
}
