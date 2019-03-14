package ink.toppest.pushserver.impl;

import com.alibaba.fastjson.JSON;
import ink.toppest.pushserver.api.ChannelAdmin;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import util.HttpUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DefaultChannelAdmin implements ChannelAdmin<Long> {

    private final Map<Long,Channel> map=new ConcurrentHashMap<>(1024);

    @Value("${server.port}")
    private String port;

    @Value("${route.address}")
    String routeAddress;

    @Override
    public void bindLocalChannel(Long aLong, Channel channel) {
        map.put(aLong,channel);
    }

    @Override
    public void bindRemoteChannel(Long aLong) {
        String server="";
        try {
            server=InetAddress.getLocalHost().getHostAddress()+":"+port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Map<String,String > map=new HashMap<>();
        map.put("server",server);
        map.put("userId",Long.toString(aLong));
        try {
            HttpUtil.post(routeAddress+"/register", JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("绑定客户端与server失败");
        }
    }


    @Override
    public Channel getChannel(Long aLong) {
        return map.get(aLong);
    }

    @Override
    public void unBindLocalChannel(Long aLong) {
        map.remove(aLong);
    }

    @Override
    public void unBindRemoteChannel(Long aLong) {
        Map<String,String > map=new HashMap<>();
        map.put("userId",Long.toString(aLong));
        try {
            HttpUtil.post(routeAddress+"/unRegister", JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("注销客户端与server失败");
        }
    }
}
