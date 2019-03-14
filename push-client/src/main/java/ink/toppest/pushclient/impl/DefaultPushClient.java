package ink.toppest.pushclient.impl;

import com.alibaba.fastjson.JSON;
import ink.toppest.pushclient.api.BroadcastPushMessage;
import ink.toppest.pushclient.api.GroupPushMessage;
import ink.toppest.pushclient.api.PushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;


/**
 * 默认实现 :发送一条消息之后就算完成.
 */
@Component
public class DefaultPushClient implements PushClient {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Resource(name = "groupDestination")
    Destination groupDestination;

    @Resource(name = "broadcastDestination")
    Destination broadcastDestination;


    @Override
    public void groupPush(GroupPushMessage message) {
        jmsTemplate.convertAndSend(groupDestination,JSON.toJSONString(message));
    }

    @Override
    public void broadcastPush(BroadcastPushMessage message) {
        jmsTemplate.convertAndSend(broadcastDestination,JSON.toJSONString(message));
    }
}
