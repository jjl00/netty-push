package ink.toppest.pushserver;


import com.google.protobuf.ByteString;
import ink.toppest.pushserver.api.ChannelAdmin;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import protocol.MessageProtocol;

import java.util.Map;

@SpringBootApplication
@RestController
@Slf4j
public class PushServerApplication implements CommandLineRunner {

    @Autowired
    ChannelAdmin<Long> channelAdmin;

    public static void main(String[] args) {
        SpringApplication.run(PushServerApplication.class, args);
    }


    @PostMapping("/push")
    public void push(@RequestBody Map<String,String> map){
        long userId=Long.parseLong(map.get("userId"));
        String message=map.get("message");
        log.info("需要向用户{}推送{}",userId,message);
        Channel channel=channelAdmin.getChannel(userId);
        channel.writeAndFlush(MessageProtocol.Protocol.newBuilder()
        .setType(MessageProtocol.Protocol.headType.PUSH)
                        .setBody(ByteString.copyFromUtf8(message)).setMessageId(userId)
                        .build());
    }

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @Override
    public void run(String... args) throws Exception {

    }
}

