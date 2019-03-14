package ink.toppest.pushzk.api;


import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;


/**
 * 管理注册在zookeeper上的server
 */
public interface ServerAdmin<T>{
    void register(T t);                  //T为server标识信息
    void unRegister(T t);                //取消注册
    List<T> getServerList();              //获取所有注册的server列表
    void subscribeChildChanges(IZkChildListener listener); //当列表改变时，进行相应地重新读取，更新缓存.
}
