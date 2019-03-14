package ink.toppest.pushclient;



import ink.toppest.pushclient.impl.DefaultBroadcastPushMessage;
import ink.toppest.pushclient.impl.DefaultGroupPushMessage;
import ink.toppest.pushclient.impl.DefaultPushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PushClientApplication implements CommandLineRunner {

    @Autowired
    DefaultPushClient defaultPushClient;

    public static void main(String[] args) {
        SpringApplication.run(PushClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        testBroadcast();
    }

    private void testGroup(){
        System.out.println("发送group消息");
        DefaultGroupPushMessage groupPushMessage=new DefaultGroupPushMessage("发送分组消息",new HashSet<Long>(){
            {add(1L);add(2L);}
        });
        defaultPushClient.groupPush(groupPushMessage);
    }

    private void testBroadcast(){
        System.out.println("发送广播消息");
        DefaultBroadcastPushMessage broadcastPushMessage=new DefaultBroadcastPushMessage("发送广播消息");
        defaultPushClient.broadcastPush(broadcastPushMessage);
    }
}
