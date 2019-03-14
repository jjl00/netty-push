package ink.toppest.pushmq.api;

public interface MessageConsumer<T>{
    void consume(T t);
}
