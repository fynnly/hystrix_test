package com.hystrix.test.thread;

import com.hystrix.test.common.BaseTest;
import com.hystrix.test.util.SpringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.junit.Test;
import org.springframework.stereotype.Component;

/**
 * 验证catch的异常不记入统计内
 */
@Component
public class ExceptionCatchBreak extends BaseTest{
    @HystrixCommand(groupKey = "ThreadTimeOut", commandKey = "mainMethod", fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "100"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "10"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "5"),
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"),
            }
    )
    public String mainMethod() throws Exception {
        System.out.println("Thread:" + Thread.currentThread().getId());

        //try {
            exceptionMethod();
//        } catch (Exception e) {
//
//        }

        System.out.println("hystrix main method end.");
        return "hello hystrix";
    }

    public void exceptionMethod() throws Exception{
        throw new Exception("Custom exception");
    }

    private String fallback( Throwable throwable) {
        System.out.println("--------->fallback");
        return "fallback";
    }


    @Test
    public void test() {
        final ExceptionCatchBreak instance = SpringUtil.getBean(ExceptionCatchBreak.class);
        for (int i = 0;i<10;i++){
            try {
                Thread.sleep(500);
                instance.mainMethod();
            } catch (Exception e) {
            }
        }

    }

}
