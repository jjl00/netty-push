package protocol;

import com.google.protobuf.ByteString;

public class ProtocolTest {
    public static void main(String[] args) throws Exception{


        MessageProtocol.Protocol protocol=MessageProtocol.Protocol
                .newBuilder().setBody(ByteString.copyFromUtf8("hello"))
                .setMessageId(1L).setType(MessageProtocol.Protocol.headType.LOGIN).build();
        System.out.println(MessageProtocol.Protocol.parseFrom(protocol.toByteArray()));
    }
}
