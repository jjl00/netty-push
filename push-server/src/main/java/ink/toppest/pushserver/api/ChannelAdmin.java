package ink.toppest.pushserver.api;

import io.netty.channel.Channel;

/**
 * 管理channel
 * @param <U>   客户端标识
 *
 */
public interface ChannelAdmin<U>{

    void bindLocalChannel(U u,Channel channel);  //保存本地channel
    void bindRemoteChannel(U u);                    //保存客户端与server之间的路由关系
    Channel getChannel(U u);                     //返回本地channel

    void unBindLocalChannel(U u);  //注销本地channel
    void unBindRemoteChannel(U u); //注销远程channel

}
