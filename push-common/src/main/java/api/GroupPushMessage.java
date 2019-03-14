package api;


import java.util.Set;

public class GroupPushMessage<U,D> extends PushMessage<D> {
    Set<U> userSet;                //需要推送的用户列表,U表示用户的标识
}
