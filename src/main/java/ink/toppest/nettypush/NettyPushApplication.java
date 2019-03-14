package ink.toppest.nettypush;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyPushApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(NettyPushApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}

