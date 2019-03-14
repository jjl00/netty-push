package ink.toppest.pushmq.impl;


import com.alibaba.fastjson.JSON;
import ink.toppest.pushmq.api.BroadcastPushMessage;
import ink.toppest.pushmq.api.GroupPushMessage;
import ink.toppest.pushmq.api.PushClient;
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
        String msg= JSON.toJSONString(message);
        System.out.println(msg);
        jmsTemplate.convertAndSend(groupDestination,msg);
    }

    @Override
    public void broadcastPush(BroadcastPushMessage message) {
        String msg= JSON.toJSONString(message);
        jmsTemplate.convertAndSend(broadcastDestination,msg);
    }
}
