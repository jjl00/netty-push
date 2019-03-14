package ink.toppest.pushroute.impl;

import ink.toppest.pushcache.api.CacheService;
import ink.toppest.pushcache.impl.DefaultCacheService;
import ink.toppest.pushroute.api.RouteService;

import ink.toppest.pushzk.api.ServerAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class DefaultRouteService implements RouteService<String,String> {
    @Autowired
    CacheService<String,String> cacheService;

    @Autowired
    ServerAdmin<String> serverAdmin;

    private volatile List<String> serverList;

    private CopyOnWriteArraySet set=new CopyOnWriteArraySet();

    private final AtomicInteger atomicInteger=new AtomicInteger(0);

    @PostConstruct
    void init(){
        log.info("路由服务初始化");
        initServerList();              //初始化server列表
    }
    private void initServerList(){
        serverList=serverAdmin.getServerList();
        serverAdmin.subscribeChildChanges((s,list)->{
            log.info("服务列表发生改变");
            serverList=list;
        });
    }


    @Override
    public String route(String s) {
        ((DefaultCacheService)cacheService).setPrefix("route-");
        return cacheService.getValue(s);
    }

    @Override
    public String getServer() throws RuntimeException{
        if(null==serverList||serverList.isEmpty()){
            throw new RuntimeException("server数量为0");
        }
        return serverList.get((atomicInteger.getAndIncrement()%serverList.size()));
    }

    @Override
    public void bindServer(String s, String s2) {
        ((DefaultCacheService)cacheService).setPrefix("route-");
        cacheService.put(s,s2);
    }

    @Override
    public void unBindServer(String s) {
        ((DefaultCacheService)cacheService).setPrefix("route-");
        cacheService.remove(s);
    }
}
