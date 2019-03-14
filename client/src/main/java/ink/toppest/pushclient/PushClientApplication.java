package ink.toppest.pushclient;


import ink.toppest.pushclient.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@SpringBootApplication
public class PushClientApplication implements CommandLineRunner {

    @Autowired
    Client client;

    public static void main(String[] args) {
        SpringApplication.run(PushClientApplication.class, args);
    }

    @GetMapping("/close")
    public void close(){
        log.info("关闭客户端");
        try {
            Client.socketChannel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/test")
    public String test(){
        return "hello";
    }


    @Override
    public void run(String... args) throws Exception {
        for(int i=0;i<100;i++){
            final long id=i;
            Thread thread=new Thread(()->{
                try {
                    client.start(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

    }
}

