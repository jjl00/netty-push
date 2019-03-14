package ink.toppest.pushclient.impl;

import ink.toppest.pushclient.api.BroadcastPushMessage;
import lombok.Data;



public class DefaultBroadcastPushMessage extends BroadcastPushMessage<String> {
    public DefaultBroadcastPushMessage(String data) {
        super(data);
    }
}
