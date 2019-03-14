package ink.toppest.pushmq.api;


import lombok.Data;



@Data
public class BroadcastPushMessage<D> extends PushMessage<D>  {
    public BroadcastPushMessage(D data) {
        super(data);
    }
}
