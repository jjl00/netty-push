package ink.toppest.pushzk.impl;

import ink.toppest.pushzk.api.ServerAdmin;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DefaultServerAdmin implements ServerAdmin<String> {
    private String registerPath;
    private ZkClient zkClient;
    @Override
    public void register(String s) {
        boolean exists = zkClient.exists(registerPath);
        if (!exists){
            //创建 root
            zkClient.createPersistent(registerPath) ;
        }
        zkClient.createEphemeral(registerPath+"/"+s);
    }


    @Override
    public void unRegister(String s) {

    }

    @Override
    public List<String> getServerList() {
        return zkClient.getChildren(registerPath);
    }

    @Override
    public void subscribeChildChanges(IZkChildListener listener) {
        zkClient.subscribeChildChanges(registerPath,listener);
    }
}
