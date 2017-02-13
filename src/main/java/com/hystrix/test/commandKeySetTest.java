package com.hystrix.test;

import com.hystrix.test.common.BaseTest;
import com.hystrix.test.util.SpringUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.junit.Test;
import org.springframework.stereotype.Component;

/**
 * 测试同一个commandKey下,哪个配置生效
 * 结论是先启动的配置生效
 */

@Component
public class CommandKeySetTest extends BaseTest{

    @HystrixCommand(groupKey = "asyncFutureTest", commandKey = "asyncFutureTestCommandKey", fallbackMethod = "startFallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "300"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "3"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "30"),
            }
    )
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
        return "success";
    }
    public String getFallback(){
        System.out.println("-------->getFallback");
        return "get failed";
    }

    @Test
    public void test(){
        final CommandKeySetTest instance = SpringUtil.getBean(CommandKeySetTest.class);
        instance.start();
        instance.get();
    }
}
