package ink.toppest.pushroute.impl;


import com.alibaba.fastjson.JSON;
import ink.toppest.pushcache.api.CacheService;
import ink.toppest.pushcache.impl.DefaultCacheService;
import ink.toppest.pushmq.api.MessageReceiver;
import ink.toppest.pushmq.impl.DefaultBroadcastPushMessage;
import ink.toppest.pushmq.impl.DefaultGroupPushMessage;
import ink.toppest.pushroute.api.PushService;
import ink.toppest.pushroute.util.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultMessageReceiver implements MessageReceiver<String,String>{


    PushService<String,String> pushService= SpringBeanFactory.getBean(PushService.class);


    CacheService<String,String> cacheService=SpringBeanFactory.getBean(CacheService.class);


    @Override
    public void receiveGroup(String s) {
        DefaultGroupPushMessage message=JSON.parseObject(s, DefaultGroupPushMessage.class);
        System.out.println(message.getUserSet());
        System.out.println(message.getData());
        message.getUserSet().parallelStream()
                .forEach((a)->{
                    Map<String,String> map=new HashMap<>();
                    map.put("userId",a.toString());
                    map.put("message",message.getData());
                    pushService.push(a.toString(),JSON.toJSONString(map));
                });
    }


    @Override
    public void receiveBroadcast(String s){
        DefaultBroadcastPushMessage message=JSON.parseObject(s,DefaultBroadcastPushMessage.class);
        log.info("需要发送广播消息: {}",message.getData());
        ((DefaultCacheService)cacheService).setPrefix("route-");
        Set<String> users=((DefaultCacheService)cacheService).getKeySet();
        log.info("需要广播的人数为:{}",users.size());
        System.out.println(users);
        users.parallelStream().forEach((a)->{
            Map<String,String> map=new HashMap<>();
            map.put("userId",a.
                    substring(6));
            map.put("message",message.getData());
            System.out.println(map);
            pushService.push(a,JSON.toJSONString(map));
        });
    }
}
