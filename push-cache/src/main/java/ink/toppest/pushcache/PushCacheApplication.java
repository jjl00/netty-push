package ink.toppest.pushcache;



import com.google.common.cache.LoadingCache;
import ink.toppest.pushcache.api.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class PushCacheApplication implements CommandLineRunner {

    @Autowired
    CacheService cacheService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    LoadingCache<String, Optional<String>> buildCache;

    public static void main(String[] args) {
        SpringApplication.run(PushCacheApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final String s="test4";
        final String v="88888888";
        System.out.println("第一次读取");
        System.out.println("local cache :"+buildCache.get(s).orElseGet(()-> {
            return "本地缓存空";
        }));
        System.out.println("redis cache :"+redisTemplate.opsForValue().get(s));
        cacheService.getValue(s,()->{
            redisTemplate.opsForValue().set(s,v);
            return v;
        });







    }
}
