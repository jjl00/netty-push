package ink.toppest.pushclient.api;


import lombok.Data;

import java.util.Set;

@Data
public class GroupPushMessage<U,D> extends PushMessage<D> {
    Set<U> userSet;                //需要推送的用户列表,U表示用户的标识

    public GroupPushMessage(D data, Set<U> userSet) {
        super(data);
        this.userSet = userSet;
    }
}
