package ink.toppest.pushclient.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String [] args){
            new NettyServer().run(Config.BEGIN_PORT, Config.END_PORT);
    }


    public void run(int beginPort, int endPort){

        System.out.println("服务端启动中");

        //配置服务端线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_REUSEADDR, true); //快速复用端口

        serverBootstrap.childHandler( new TcpCountHandler());


        for(; beginPort < endPort; beginPort++){
            int port = beginPort;
            serverBootstrap.bind(port).addListener((ChannelFutureListener) future->{
                System.out.println("服务端成功绑定端口 port = "+port);
            });
        }



    }


}
