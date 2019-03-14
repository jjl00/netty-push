package ink.toppest.pushroute;





import ink.toppest.pushcache.api.CacheService;
import ink.toppest.pushmq.api.MessageReceiver;
import ink.toppest.pushroute.api.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class PushRouteApplication implements CommandLineRunner {

    @Autowired
    RouteService<String,String> routeService;

    @Autowired
    MessageReceiver messageReceiver;

    @Autowired
    CacheService<String,String> cacheService;


    public static void main(String[] args) {
        SpringApplication.run(PushRouteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

//    @GetMapping("/test")
//    public String test(){
//        Thread thread=new Thread(()->{
//            System.out.println(routeUtil.getNextRoute());
//        });
//        thread.start();
//        return "ok";
//    }
}

