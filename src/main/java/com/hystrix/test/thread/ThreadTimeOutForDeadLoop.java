package com.hystrix.test.thread;

import com.hystrix.test.common.BaseTest;
import com.hystrix.test.util.CommonThreadPool;
import com.hystrix.test.util.IThreadWork;
import com.hystrix.test.util.SpringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.junit.Test;
import org.springframework.stereotype.Component;

/**
 *
 *  测试线程池模式下
 *  使用while(true)超时时,是否会释放线程
 *
 */
@Component
public class ThreadTimeOutForDeadLoop extends BaseTest {
    @HystrixCommand(groupKey = "ThreadTimeOut", commandKey = "mainMethod", fallbackMethod = "fallback",
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
    public String mainMethod() {
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


    @Test
    public void test() {
        final ThreadTimeOutForDeadLoop instance = SpringUtil.getBean(ThreadTimeOutForDeadLoop.class);
        CommonThreadPool threadPool = CommonThreadPool.getThreadPool();
        for (int i = 0;i<10;i++){
            try{
                Thread.sleep(1000);
                threadPool.addWork(new IThreadWork() {
                    @Override
                    public void doWork() {
                        instance.mainMethod();
                    }
                });
            }catch (Exception ex){

            }
        }
    }

}
