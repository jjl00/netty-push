//package ink.toppest.pushserver.util;
//
//import org.I0Itec.zkclient.ZkClient;
//import util.Constants;
//
//public class ZKUtil {
//    private static final ZkClient zkClient=BeanUtil.getBean(ZkClient.class);
//
//    public static void createRootNode(){
//        if(!zkClient.exists(Constants.ZK_ROOT_NODE)){
//            zkClient.createPersistent(Constants.ZK_ROOT_NODE);
//        }
//    }
//
//    public static void createTempNode(String path){
//        zkClient.createEphemeral(path);
//    }
//
//}
