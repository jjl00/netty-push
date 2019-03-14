package ink.toppest.pushserver.server;

import ink.toppest.pushserver.initializer.ServerInitializer;
import ink.toppest.pushzk.api.ServerAdmin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class Server {
    private ServerBootstrap serverBootstrap=new ServerBootstrap();
    private EventLoopGroup bossGroup=new NioEventLoopGroup(1);
    private EventLoopGroup workerGruop=new NioEventLoopGroup();

    @Autowired
    private ServerInitializer initializer;

    @Value("${port}")
    int port;
    @Autowired
    ServerAdmin<String> serverAdmin;



    @PostConstruct
    private void start() throws InterruptedException{
        serverBootstrap.group(workerGruop,bossGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_REUSEADDR, true) //快速复用端口
                .childHandler(initializer);
        ChannelFuture channelFuture=serverBootstrap.bind().sync();
        if(channelFuture.isSuccess()){
            log.info("服务器启动成功,绑定端口: {}",port);
            doRegister();
        }
    }
    private void doRegister(){
        Thread thread=new Thread(()->{
            String addr="127.0.0.1";
            try {
                addr = InetAddress.getLocalHost().getHostAddress();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            }
            serverAdmin.register(addr+":"+port);
        });
        thread.setName("register zookeeper");
        thread.start();
    }

}
