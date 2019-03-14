package ink.toppest.pushserver.impl;

import ink.toppest.pushserver.api.AuthenticationStrategy;
import ink.toppest.pushserver.api.Authenticator;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.ServiceLoader;

//默认认证器，只要认证策略满足一个，即认证成功.
@Component
public class DefaultAuthenticator<K,V> implements Authenticator<K,V> {


    @Override
    public boolean authenticate(K k, V v) {
        Iterator<AuthenticationStrategy> iterator =
                ServiceLoader.load(AuthenticationStrategy.class).iterator();
        boolean result=false;
        while (iterator.hasNext()){                      //认证策略通过java spi机制确认.
            if(iterator.next().authenticate(k,v)){
                result=true;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        DefaultAuthenticator<Long,String> authenticator=new DefaultAuthenticator<>();
        System.out.println(authenticator.authenticate(1L,"token"));
    }
}
