package ink.toppest.pushroute.controller;


import com.alibaba.fastjson.JSON;
import ink.toppest.pushcache.api.CacheService;
import ink.toppest.pushcache.impl.DefaultCacheService;
import ink.toppest.pushroute.api.PushService;
import ink.toppest.pushroute.api.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class RouteController {
    @Autowired
    RouteService<String,String> routeService;

    @Autowired
    PushService<String,String> pushService;

    @Autowired
    CacheService<String,String> cacheService;



    @GetMapping("/server")
    public String getServer(){
        return routeService.getServer();
    }

    @PostMapping("/register")
    public void register(@RequestBody Map<String,String> map){
        log.info("{}注册到{}",map.get("userId"),map.get("server"));
        routeService.bindServer(map.get("userId"),map.get("server"));
    }

    @PostMapping("/unRegister")
    public void unRegister(@RequestBody Map<String,String> map){
        log.info("注销用户{}",map.get("userId"));
        routeService.unBindServer(map.get("userId"));
    }


    @GetMapping("/push")
    public void push(){
        ((DefaultCacheService)cacheService).setPrefix("route-");
        Set<String> users=((DefaultCacheService)cacheService).getKeySet();
        users.parallelStream().forEach((a)->{
            Map<String,String> map=new HashMap<>();
            map.put("userId",a.
                    substring(6));
            map.put("message","http测试...");
            pushService.push(a, JSON.toJSONString(map));
        });
    }


}
