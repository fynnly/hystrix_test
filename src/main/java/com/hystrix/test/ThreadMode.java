package com.hystrix.test;

import com.hystrix.test.util.CommonThreadPool;
import com.hystrix.test.util.IThreadWork;
import com.hystrix.test.util.SpringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by libin on 17/1/23.
 */
@Service
public class ThreadMode {
    @HystrixCommand(groupKey = "hystrixTestGroup", commandKey = "hystrixcommandKey", fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "100"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_CLOSED, value = "true"),
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "3"),
                    @HystrixProperty(name = "maxQueueSize", value = "3")
            }
    )
    public String getName() {
        System.out.println("Thread:" + Thread.currentThread().getId());
        while(true){
            //死循环里面这个逻辑可以忽略,貌似不写就不让我编译通过
            int i=0;
            if(i>0){
                break;
            }
        }

        System.out.println("hystrix main method end.");
        return "hello hystrix";
    }

    private String fallback( Throwable throwable) {
        System.out.println("--------->fallback");
        return "fallback";
    }


    public static void main(String[] args) {
        SpringUtil.init();
        final ThreadMode threadMode = SpringUtil.getBean(ThreadMode.class);
        CommonThreadPool threadPool = CommonThreadPool.getThreadPool();
        for (int i = 0;i<10;i++){
            try{
                Thread.sleep(1000);
                threadPool.addWork(new IThreadWork() {
                    @Override
                    public void doWork() {
                        threadMode.getName();
                    }
                });
            }catch (Exception ex){

            }
        }
    }


}
