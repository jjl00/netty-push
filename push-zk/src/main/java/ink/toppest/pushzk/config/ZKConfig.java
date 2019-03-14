package ink.toppest.pushzk.config;

import ink.toppest.pushzk.api.ServerAdmin;
import ink.toppest.pushzk.impl.DefaultServerAdmin;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ZKConfig {

    public static final String zkAddress="127.0.0.1:2181";


    @Value("${zk.register_path}")
    public static final String rootPath="/server";


    @Bean
    ZkClient zkClient(){
        return new ZkClient(zkAddress,5000);
    }

    @Bean
    ServerAdmin serverAdmin(){
        return new DefaultServerAdmin(rootPath,zkClient());
    }
}
