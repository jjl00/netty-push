package ink.toppest.pushcache.api;

import java.util.Set;
import java.util.function.Supplier;

public interface CacheService<K,V>{
    V getValue(K k);
    void put(K k,V v);
    V getValue(K k, Supplier<? extends V> supplier);
    void remove(K k);
}
