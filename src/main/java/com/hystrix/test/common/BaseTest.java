package com.hystrix.test.common;

import com.hystrix.test.util.SpringUtil;
import org.junit.Before;

/**
 * Created by libin on 17/1/24.
 */
public class BaseTest {
    @Before
    public void setUp() throws Exception {
        SpringUtil.init();
    }

}
