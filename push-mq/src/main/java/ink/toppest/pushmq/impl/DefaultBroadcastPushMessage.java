package ink.toppest.pushmq.impl;


import ink.toppest.pushmq.api.BroadcastPushMessage;

import java.io.Serializable;

public class DefaultBroadcastPushMessage extends BroadcastPushMessage<String>  {
    public DefaultBroadcastPushMessage(String data) {
        super(data);
    }
}
