package ink.toppest.pushclient.client;

import ink.toppest.pushclient.handler.ClientHandler;
import ink.toppest.pushclient.initializer.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class Client{
    @Autowired
    OkHttpClient httpClient;

    @Value("${route.address}")
    private String routeAddress;

    public static SocketChannel socketChannel;

    public void start(long id) throws InterruptedException{
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .option(ChannelOption.SO_REUSEADDR,true)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer(new ClientHandler(id)));
        String server=getServerAddress();
        log.info("用户{}得到{}",id,server);
        ChannelFuture channelFuture = bootstrap.connect(server.split(":")[0],
                Integer.valueOf(server.split(":")[1]));
        try {
            channelFuture.addListener((ChannelFutureListener) future ->{
                if(!future.isSuccess()){
                    log.info("用户{}连接失败",id);
                    final EventLoop eventLoop = future.channel().eventLoop();
                    eventLoop.schedule(()->{
                        try {
                            start(id);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, 1L, TimeUnit.SECONDS);
                }else{
                    log.info("用户{}连接{}:{}成功",id,server.split(":")[0]
                            ,server.split(":")[1]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void start(long id) throws InterruptedException{
//
//        try{
//            getServerAddress();
//            String[] ipPort = serverAddress.orElseGet(() -> {
//                getServerAddress();
//                return serverAddress.get();
//            }).split(":");
//            bootstrap.group(workerGroup)
//                    .option(ChannelOption.SO_REUSEADDR,true)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ClientInitializer(new ClientHandler(id)));
//
//            ChannelFuture channelFuture = bootstrap.connect(ipPort[0], Integer.valueOf(ipPort[1])).sync();
//            if (channelFuture.isSuccess()) {
//                log.info("成功连接上{}", ipPort[0] + ":" + ipPort[1]);
//            }
//            socketChannel = (SocketChannel) channelFuture.channel();
// //           channelFuture.channel().closeFuture().sync();
//        }finally {
// //           workerGroup.shutdownGracefully().sync();
//        }
//
//    }

    private String getServerAddress(){

        Request request = new Request.Builder()
                .url("http://"+routeAddress+"/server")
                .get()
                .build();
        String serverAddress=null;
        try {
            serverAddress=httpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverAddress;
    }
}
