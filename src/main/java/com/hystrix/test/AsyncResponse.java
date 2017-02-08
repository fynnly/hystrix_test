package com.hystrix.test;

import java.util.concurrent.Future;

/**
 * Created by libin on 16/9/13.
 */
public class AsyncResponse {
    private Future future;
    private boolean isSwitchOn;
    /**请求异步接口的开始时间，毫秒级，用来计算阻塞get的超时时间*/
    private long startTime;

    public AsyncResponse(boolean isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public boolean isSwitchOn() {
        return isSwitchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        isSwitchOn = switchOn;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
