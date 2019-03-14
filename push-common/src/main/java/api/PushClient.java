package api;

/**
 * 应用推送入口
 */
public interface PushClient {
    void groupPush(GroupPushMessage message);
    void broadcastPush(BroadcastPushMessage message);
}
