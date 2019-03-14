package ink.toppest.pushroute.api;

/**
 * @param <D> 数据
 * @param <S> server标识
 */
public interface PushService<S,D>{
    void push(S s,D d);
}
