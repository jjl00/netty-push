package ink.toppest.pushclient.impl;

import ink.toppest.pushclient.api.GroupPushMessage;
import lombok.Data;
import java.util.Set;


@Data
public class DefaultGroupPushMessage extends GroupPushMessage<Long,String> {
    public DefaultGroupPushMessage(String data, Set<Long> userSet) {
        super(data, userSet);
    }
}
