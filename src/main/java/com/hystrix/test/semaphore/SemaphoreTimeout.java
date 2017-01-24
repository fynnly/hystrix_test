package com.hystrix.test.semaphore;

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
 * 测试信号量模式下超时后,是否立即回收信号量,
 * 回收信号量后,是否释放线程
 */
@Component
public class SemaphoreTimeout extends BaseTest {
    @HystrixCommand(groupKey = "SemaphoreTimeout", commandKey = "mainMethod", fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "100"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, value = "3"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_CLOSED, value = "true"),
            }
    )
    public String mainMethod() {
        System.out.println("Thread:" + Thread.currentThread().getId());
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hystrix main method end.");
        return "hello hystrix";
    }

    private String fallback(Throwable throwable) {
        System.out.println("--------->fallback");
        return "fallback";
    }

    //调用方法如下，启动10个线程，第个线程间隔1000ms，确保前一个信号量已经因为超时释放出来
    @Test
    public void test(){
        final SemaphoreTimeout instance = SpringUtil.getBean(SemaphoreTimeout.class);
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
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
