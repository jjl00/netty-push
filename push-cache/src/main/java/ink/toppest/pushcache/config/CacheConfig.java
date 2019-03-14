package ink.toppest.pushcache.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ink.toppest.pushcache.api.CacheService;
import ink.toppest.pushcache.impl.DefaultCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class CacheConfig{
    @Bean
    @ConditionalOnProperty(
            value = {"cache.local"},
            matchIfMissing = false)
    public LoadingCache<String,Optional<String>> buildCache(){
        return CacheBuilder.newBuilder().maximumSize(1024)
                .build(new CacheLoader<String,Optional<String>>() {
                    @Override
                    public Optional<String> load(String s) throws Exception {
                        return Optional.empty();
                    }
                });
    }

    @Bean
    CacheService<String,String> cacheService(){
        return new DefaultCacheService("");
    }


}
