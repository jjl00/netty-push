package ink.toppest.pushserver.api;

/**
 *  认证器，通过调用认证策略，来完成认证.
 * @param <K>  用户身份
 * @param <V>  用户凭证
 */
public interface Authenticator<K,V>{
    boolean authenticate(K k,V v);
}
