package ink.toppest.pushclient.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutionException;

public class NettyClient {


    private static final String SERVER = "127.0.0.1";

    public static void main(String [] args){
        new NettyClient().run(Config.BEGIN_PORT, Config.END_PORT);
    }

    public void run(int beginPort, int endPort){

        System.out.println("客户端启动中");

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }

                });


        int index = 0 ;
        int finalPort ;
        while (true){

            finalPort = beginPort + index;

            try {
                bootstrap.connect(SERVER, finalPort).addListener((ChannelFutureListener)future ->{
                    if(!future.isSuccess()){
                        System.out.println("创建连接失败 " );
                    }
                }).get();

                System.out.println("port = "+ finalPort);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            ++index;
            if(index == (endPort - beginPort)){
                index = 0 ;
            }

        }



    }



}
