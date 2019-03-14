package ink.toppest.pushroute.api;

/**
 *   根据用户标识找出其位于哪个server下
 * @param <K> K为用户标识
 * @param <V> V为server标识
 */
public interface RouteService<K,V>{
    V route(K k);
    V getServer();
    void bindServer(K k,V v);
    void unBindServer(K k);
}
