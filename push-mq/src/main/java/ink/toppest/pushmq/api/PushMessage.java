package ink.toppest.pushmq.api;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;


@JsonAutoDetect
@Data
public  class PushMessage<D> {
    D data;                              //要推送的数据

    public PushMessage(D data) {
        this.data = data;
    }
}
