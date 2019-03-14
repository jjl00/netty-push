package ink.toppest.pushmq.api;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@JsonAutoDetect
public class GroupPushMessage<U,D> extends PushMessage<D> {
    Set<U> userSet;                //需要推送的用户列表,U表示用户的标识

    public GroupPushMessage(D data, Set<U> userSet) {
        super(data);
        this.userSet = userSet;
    }
}
