package ink.toppest.pushserver.impl;

import ink.toppest.pushserver.api.AuthenticationStrategy;

public class DefaultAuthenticationStrategy implements AuthenticationStrategy<Long,String>{


    @Override
    public boolean authenticate(Long aLong, String s) {
        return  s.equals("token");
    }
}
