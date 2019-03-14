package util;

/**
 * 消息类型
 */
public enum MsgType {
    LOGIN(1),
    PING(2),
    PUSH(3);

    public final int value;
    MsgType(int value){
        this.value=value;
    }
}
