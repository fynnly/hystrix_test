package com.hystrix.test.util;

import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by libin on 17/1/24.
 */
public class CommonThreadPool {
    // 线程池维护线程的数量
    private final static int CORE_POOL_SIZE = 10;
    private final static int MAX_QUEUE_SIZE = 5000;

    private final static int MAX_POOL_SIZE = 50;


    public static CommonThreadPool getThreadPool(){
        return new CommonThreadPool();
    }
    /**
     * 缓冲队列
     */
    private ArrayBlockingQueue<Runnable> workQueue =new ArrayBlockingQueue<Runnable>(MAX_QUEUE_SIZE);
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,10L, TimeUnit.MINUTES,workQueue);
    /**
     * DOCUMENT ME!
     *
     * @param work DOCUMENT ME!
     */
    public void addWork(IThreadWork work ) {
        Runnable task = new CommonWorkThread(work);
        threadPool.execute(task);
    }

    /**
     * DOCUMENT ME!
     */
    @PreDestroy
    public void releaseThreadPool() {
        threadPool.shutdown();
    }

    class CommonWorkThread implements Runnable {
        IThreadWork work = null;

        public CommonWorkThread(IThreadWork work) {
            this.work = work;
        }

        public void setWork(IThreadWork work) {
            this.work = work;
        }

        public IThreadWork getWork() {
            return this.work;
        }

        public void run() {
            try{
                if (work != null) {
                    work.doWork();
                    // work = null;
                }
            } catch (Exception e2) {
                // 不再处理了
            }
        }
    }
}
