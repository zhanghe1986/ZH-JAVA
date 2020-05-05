package com.dahe.base.common.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.util.Assert;

public class BaseThreadPoolExecutor {

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1195201938418350881L;
    /* A core tread size in the thread pool */
    private static int corePoolSize = 20;
    /* A max tread size in the thread pool */
    private static int maxPoolSize = 30;
    /* An idle thread keep alive time */
    private static int keepAliveTime = 60;
    private static boolean allowCoreThreadTimeOut = false;
    /* A work queue capacity in the thread pool */
    private static int queueCapacity = Integer.MAX_VALUE;
    /* An ExecutorService that executes each submitted task using one of possibly several pooled threads, normally configured using Executors factory methods */
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * static structure to initialize tread pool executor
     * initialize work queue
     * initialize threadpoolexecutor (corePoolSize, maxPoolSize, keepAliveTime, queue)
     * initialize allowCoreThreadTimeOut
     * */
    static {
        BlockingQueue<Runnable> queue = createQueue(queueCapacity);
        threadPoolExecutor  = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                queue);
        if (allowCoreThreadTimeOut) {
        	threadPoolExecutor.allowCoreThreadTimeOut(true);
        }
    }
    
    /**
     * Create the BlockingQueue to use for the ThreadPoolExecutor.
     * <p>A LinkedBlockingQueue instance will be created for a positive
     * capacity value; a SynchronousQueue else.
     * @param queueCapacity the specified queue capacity
     * @return the BlockingQueue instance
     * @see java.util.concurrent.LinkedBlockingQueue
     * @see java.util.concurrent.SynchronousQueue
     */
    private static BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<Runnable>(queueCapacity);
        }
        else {
            return new SynchronousQueue<Runnable>();
        }
    }

    /**
     * execute a runnable interface task
     * @param task
     * */
    public static void execute(Runnable task) {
        Executor executor = getThreadPoolExecutor();
        try {
            executor.execute(task);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("[BaseThreadPoolExecutor]Executor [" + executor + "] does not accept task:" + task, ex);
        }
    }

    public static Future<?> submit(Runnable task) {
        ExecutorService executor = getThreadPoolExecutor();
        try {
            return executor.submit(task);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("[BaseThreadPoolExecutor]Executor [" + executor + "] does not accept task: " + task, ex);
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        ExecutorService executor = getThreadPoolExecutor();
        try {
            return executor.submit(task);
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("[BaseThreadPoolExecutor]Executor [" + executor + "] does not accept task: " + task, ex);
        }
    }
    
    /**
     * Return the underlying ThreadPoolExecutor for native access.
     * @return the underlying ThreadPoolExecutor (never <code>null</code>)
     * @throws IllegalStateException if the ThreadPoolTaskExecutor hasn't been initialized yet
     */
    private static ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
        Assert.state(threadPoolExecutor != null, "ThreadPoolTaskExecutor not initialized");
        return threadPoolExecutor;
    }

}