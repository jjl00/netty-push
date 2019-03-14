package ink.toppest.pushmq.api;


public interface MessageReceiver<G,B>{
    void receiveGroup(G g);
    void receiveBroadcast(B b);
}


