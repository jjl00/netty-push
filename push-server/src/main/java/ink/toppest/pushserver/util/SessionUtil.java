package ink.toppest.pushserver.util;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    public static final Map<Long, NioSocketChannel> CHANNEL_MAP=new ConcurrentHashMap<>(16);
    public static final Map<Long,String> USER_INFO_MAP=new ConcurrentHashMap<>(16);

}
