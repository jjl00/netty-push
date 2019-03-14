package ink.toppest.pushserver.handler;



import ink.toppest.pushserver.api.Authenticator;
import ink.toppest.pushserver.api.ChannelAdmin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import protocol.MessageProtocol;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;


@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol.Protocol> {

    public static AttributeKey<Long> attr_userId=AttributeKey.valueOf("userId");

    @Autowired
    Authenticator<Long,String> authenticator;

    @Autowired
    MessageProtocol.Protocol authenticated;

    @Autowired
    ChannelAdmin<Long> channelAdmin;

    @Resource(name = "singleThreadExecutor")
    ExecutorService executorService;

    private ThreadLocal<Integer> unReadCount=new ThreadLocal<>();



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol.Protocol protocol) throws Exception {
        switch (protocol.getType()) {
            case LOGIN:
                doLogin(channelHandlerContext, protocol);
                break;
            case PING:
                log.info("用户{}发送心跳",channelHandlerContext.channel().attr(attr_userId).get());
                //本来需要双方维持心跳，即一方心跳超时，便断线或者重连.客户端在心跳检测的代码实现断线重连(超时未收到服务端的ping).
                // 但是服务器心跳超时，很可能是由于服务器宕机，而服务器宕机IdleStateHandler
                //会调用channelInactive()方法，取消之前的心跳处理操作,所以客户端判断服务端超时的代码不会执行
                // 我们需要自己实现一个IdleStateHandler,重写channelInactive()
                //或者将重连逻辑写在业务handler里的channelInactive()
                break;
            default:
                throw new RuntimeException("消息类型不正确");


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //异常时断开连接
        log.info("发生异常........");

        doUnregister(Long.parseLong(ctx.channel().attr(attr_userId).get().toString()));
        cause.printStackTrace() ;
        ctx.close();
    }


    private void doLogin(ChannelHandlerContext channelHandlerContext, MessageProtocol.Protocol protocol) throws UnsupportedEncodingException {
        if(authenticator.authenticate(protocol.getMessageId(),protocol.getBody().toString("UTF-8"))){
            log.info("用户{}认证成功",protocol.getMessageId());
            channelHandlerContext.writeAndFlush(authenticated);
            executorService.execute(()->{
                log.info("用户{}准备注册",protocol.getMessageId());
                doRegister(protocol.getMessageId(),channelHandlerContext.channel());
            });
            return;
        }
        log.info("用户{}认证失败",protocol.getMessageId());
        try {
            channelHandlerContext.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doRegister(Long userId, Channel channel){
        channelAdmin.bindRemoteChannel(userId);             //将channel与server注册到路由层
        channelAdmin.bindLocalChannel(userId,channel);      //保存本地channel
//        attr_userId=AttributeKey.valueOf(userId.toString());   //将userId保存起来
        channel.attr(attr_userId).set(userId);                              //将userId保存起来
        log.info("注册完毕");
    }

    private void doUnregister(Long userId){
        channelAdmin.unBindRemoteChannel(userId);             //将channel与server注册到路由层
        channelAdmin.unBindLocalChannel(userId);      //保存本地channel
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){                   //维护用户心跳
            IdleStateEvent event=(IdleStateEvent) evt;
            if(IdleState.READER_IDLE==event.state()){
                unReadCount.set(unReadCount.get()+1);
                if(1==unReadCount.get()){
                    doUnregister(Long.parseLong(ctx.channel().attr(attr_userId).get().toString()));
                }
                ctx.channel().close().sync();

            }
        }
        super.userEventTriggered(ctx,evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("用户{}channelInactive",ctx.channel().attr(attr_userId).get());
        super.channelInactive(ctx);

        doUnregister(Long.parseLong(ctx.channel().attr(attr_userId).get().toString()));
    }
}

