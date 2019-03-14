package ink.toppest.pushclient.handler;



import com.google.protobuf.ByteString;
import ink.toppest.pushclient.client.Client;
import ink.toppest.pushclient.util.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import protocol.MessageProtocol;
import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol.Protocol> {


    Client client= SpringBeanFactory.getBean(Client.class);

    MessageProtocol.Protocol heartBeat=SpringBeanFactory.getBean("heartBeat",MessageProtocol.Protocol.class);
    
    long id;
    
    String token="token";

    public ClientHandler(long id){
        this.id=id;
    }

   
    ScheduledExecutorService executorService=SpringBeanFactory.getBean("singleThreadScheduledExecutor",ScheduledExecutorService.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        log.info("用户{}断线",id);
        super.channelInactive(ctx);
//        使用过程中断线重连
        executorService.schedule(()->{
            try {
                log.info("用户{}准备重连",id);
                client.start(id);
            } catch (InterruptedException e) {
                log.info("重连异常: ",e.getMessage());
            }
        }, 1, TimeUnit.SECONDS);

//        log.info("用户{}准备重连",id);
//        new Thread(()->{
//            try {
//                client.start();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,MessageProtocol.Protocol protocol) throws Exception {
        switch (protocol.getType()){
            case AUTHENTICATED:
                log.info("用户{}认证成功",id);
                break;
            case PUSH:
                log.info("用户{}收到消息: {}",id,protocol.getBody().toString("UTF-8"));
                break;
            default:
                break;
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){                   //维护用户心跳
            IdleStateEvent event=(IdleStateEvent) evt;
            if(IdleState.WRITER_IDLE==event.state()){
                log.info("用户{}发送心跳包",id);
                ctx.channel().writeAndFlush(heartBeat);
            }
        }
        super.userEventTriggered(ctx,evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);                       //将消息继续传下去
        log.info("客户端{}发送login请求",id);
        ctx.writeAndFlush(MessageProtocol.Protocol
                .newBuilder()
                .setBody(ByteString.copyFromUtf8(token))
                .setMessageId(id)
                .setType(MessageProtocol.Protocol.headType.LOGIN)
                .build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        log.info("用户{}发生异常",id);
        cause.printStackTrace() ;
        ctx.close() ;
    }
}
