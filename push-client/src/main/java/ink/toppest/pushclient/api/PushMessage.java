package ink.toppest.pushclient.api;


import lombok.Data;

@Data

public  class PushMessage<D>{
    D data;                              //要推送的数据

    public PushMessage(D data) {
        this.data = data;
    }
}
