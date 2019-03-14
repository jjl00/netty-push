package ink.toppest.pushserver.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware{
    private  static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }

    public static <T> T getBean(Class<T> c){
        return context.getBean(c);
    }

    public static <T> T getBean(String name,Class<T> c){
        return context.getBean(name,c);
    }

}
