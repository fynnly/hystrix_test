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

    @HystrixCommand(groupKey = "CommandKeySetTest", commandKey = "method", fallbackMethod = "method1Fallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "300"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "3"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "30"),
            }
    )
    public void method1(){
        System.out.println("-------->method1");
    }
    public void method1Fallback(){
        System.out.println("-------->method1Fallback");
    }

    @HystrixCommand(groupKey = "CommandKeySetTest", commandKey = "method2", fallbackMethod = "method2Fallback",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "100"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "2"),
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "10"),
            }
    )
    public void method2(){
        System.out.println("-------->method2");
    }
    public void method2Fallback(){
        System.out.println("-------->method2Fallback");
    }

    @Test
    public void test(){
        final CommandKeySetTest instance = SpringUtil.getBean(CommandKeySetTest.class);
        instance.method1();
        instance.method2();
    }
}
