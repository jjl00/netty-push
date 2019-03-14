package ink.toppest.pushserver.api;

/**
 * 认证策略，用于认证用户身份是否合理
 */
public interface AuthenticationStrategy<K,V>{
    boolean authenticate(K k,V v);      //K为用户身份，V为用户凭证
}
