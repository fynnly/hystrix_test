package com.hystrix.test;

import com.hystrix.test.common.BaseTest;
import com.hystrix.test.util.CommonThreadPool;
import com.hystrix.test.util.IThreadWork;
import com.hystrix.test.util.SpringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * Created by libin on 17/1/25.
 */

@Component
public class AsyncFutureTest extends BaseTest{

    @HystrixCommand(groupKey = "asyncFutureTest", commandKey = "asyncFutureTestCommandKey", fallbackMethod = "startFallback")
    public AsyncResponse start(){
        System.out.println("-------->start");
        return null;
    }
    public AsyncResponse startFallback(){
        System.out.println("-------->startFallback");

        return null;
    }

    @HystrixCommand(groupKey = "asyncFutureTest", commandKey = "asyncFutureTestCommandKey", fallbackMethod = "getFallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "100"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "2"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "10"),
            }
    )
    public String get(){
        System.out.println("-------->get");
        int i =1/0;
        return "success";
    }
    public String getFallback(){
        System.out.println("-------->getFallback");
        return "get failed";
    }

    @Test
    public void test(){
        final AsyncFutureTest instance = SpringUtil.getBean(AsyncFutureTest.class);
        for (int i = 0;i<10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            instance.get();
            instance.start();
        }
//        CommonThreadPool threadPool = CommonThreadPool.getThreadPool();
//        for (int i = 0;i<1;i++){
//            try{
//                Thread.sleep(1000);
//                threadPool.addWork(new IThreadWork() {
//                    @Override
//                    public void doWork() {
//                        instance.get();
//                        instance.start();
//                    }
//                });
//            }catch (Exception ex){
//
//            }
//        }
//        for (int i = 0;i<10;i++){
//            instance.start();
//        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
    }
}
