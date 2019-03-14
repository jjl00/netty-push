package ink.toppest.pushclient.client;

import ink.toppest.pushclient.handler.ClientHandler;
import ink.toppest.pushclient.initializer.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class Client{
    @Autowired
    OkHttpClient httpClient;




    public static SocketChannel socketChannel;

    @Value("${route.address}")
    private String routeAddress;



    private Optional<String> serverAddress;


    public void start(long id)throws InterruptedException {
        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            getServerAddress();
            String[] ipPort = serverAddress.orElseGet(() -> {
                getServerAddress();
                return serverAddress.get();
            }).split(":");
            bootstrap.group(workerGroup)
                    .option(ChannelOption.SO_REUSEADDR,true)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer(new ClientHandler(id)));

            ChannelFuture channelFuture = bootstrap.connect(ipPort[0], Integer.valueOf(ipPort[1])).sync();
            if (channelFuture.isSuccess()) {
                log.info("成功连接上{}", ipPort[0] + ":" + ipPort[1]);
            }
            socketChannel = (SocketChannel) channelFuture.channel();
            channelFuture.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully().sync();
        }

    }

    private void getServerAddress (){
        Request request = new Request.Builder()
                .url("http://"+routeAddress+"/server")
                .get()
                .build();
        try {
            serverAddress=Optional.of(httpClient.newCall(request).execute().body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
