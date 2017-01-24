package com.hystrix.test.util;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * Created by libin on 16/6/2.
 */
public class SpringUtil {


    private static ApplicationContext applicationContext;

    private static Object initLock = new Object();

    public static <T> T getBean(Class<T> requiredType){
        return getApplicationContext().getBean(requiredType);
    }

    private static ApplicationContext getApplicationContext(){
        if (applicationContext == null){
            synchronized(SpringUtil.class){
                if(applicationContext == null){
                    applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
                }
            }
        }
        return applicationContext;
    }
    public static void init(){
        getApplicationContext();
    }


}
