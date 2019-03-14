package ink.toppest.pushcache.impl;

import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;
import ink.toppest.pushcache.api.CacheService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@AllArgsConstructor
@NoArgsConstructor
public class DefaultCacheService implements CacheService<String,String> {

    @Autowired(required = false)
    LoadingCache<String,Optional<String>> cache;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    private ThreadLocal<String> prefix=new ThreadLocal<>();

    public DefaultCacheService(String prefix) {
        this.prefix.set(prefix);
    }



    public void setPrefix(String prefix) {
        this.prefix.set(prefix);
    }

    public String getPrefix(){
        return prefix.get();
    }

    @Override
    public String getValue(String s) throws RuntimeException{
        return getValue(s,()->null);
    }

    @Override
    public void put(String s, String s2) {
        s=prefix.get()+s;
        cache.put(s,Optional.ofNullable(s2));
        redisTemplate.opsForValue().set(s,s2);
    }

    @Override
    public String getValue(String s, Supplier<? extends String> supplier) {
        Preconditions.checkNotNull(s,"缓存key的前缀不能为空");
        final String s2=s;
        Optional<String> result;
        try {
            result=cache.get(s2);            //先从本地缓存读取
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取本地缓存异常",e);
        }

        return result.orElseGet(()->{
            final Optional<String> res=Optional.ofNullable(redisTemplate.opsForValue().get(s2));//从redis中读取,由于redis是单线程，故不需要保证同步与可见性
            if(res.isPresent()){
                cache.put(s2,res);                         //redis能读取到
                return res.get();
            }
            return res.orElseGet(supplier);          //从最终策略读取，可能需要注意同步的问题,以及需要更新redis缓存.
        });
    }

    @Override
    public void remove(String s) {
        s=prefix.get()+s;
        cache.invalidate(s);
        redisTemplate.delete(s);
    }

    public Set<String> getKeySet(){
        return redisTemplate.keys(prefix.get()+"*");
    }


}
