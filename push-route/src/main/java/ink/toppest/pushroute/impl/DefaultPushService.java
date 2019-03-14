package ink.toppest.pushroute.impl;

import ink.toppest.pushroute.api.PushService;
import ink.toppest.pushroute.api.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.HttpUtil;

@Component
@Slf4j
public class DefaultPushService implements PushService<String,String> {

    @Autowired
    RouteService<String,String> routeService;

    @Override
    public void push(String s, String s2) {
        String server=routeService.route(s);
        try {
            HttpUtil.post(server+"/push",s2);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("推送消息失败");
        }
    }
}
