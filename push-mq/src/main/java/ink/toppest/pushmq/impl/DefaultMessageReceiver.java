package ink.toppest.pushmq.impl;


import ink.toppest.pushmq.api.MessageReceiver;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.annotation.JmsListener;

import java.util.Iterator;
import java.util.ServiceLoader;

@AllArgsConstructor
public class DefaultMessageReceiver implements MessageReceiver<String, String> {


    @JmsListener(destination ="push_group")
    public void receiveGroup(String groupPushMessage) {
        Iterator<MessageReceiver> iterator=ServiceLoader.load(MessageReceiver.class).iterator();

        while (iterator.hasNext()){
            iterator.next().receiveGroup(groupPushMessage);
        }

    }

    @JmsListener(destination = "push_broadcast")
    public void receiveBroadcast(String broadcastPushMessage) {
        Iterator<MessageReceiver> iterator=ServiceLoader.load(MessageReceiver.class).iterator();
        while (iterator.hasNext()){
            iterator.next().receiveBroadcast(broadcastPushMessage);
        }

    }

}
