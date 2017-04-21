package org.spacebison.androidutils.concurrent;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cmb on 03.03.17.
 */

public final class Concurrent {
    private Concurrent() {
    }

    public static ExecutorService getCommonExecutor() {
        return CommonExecutorHolder.EXECUTOR;
    }

    public static Executor getMainThreadExecutor() {
        return UiThreadHolder.HANDLER_EXECUTOR;
    }

    public static ExecutorService newPreemptiveSingleThreadExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return threadPoolExecutor;
    }

    public static ExecutorService newPreemptiveSingleThreadExecutor(ThreadFactory threadFactory) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), threadFactory);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return threadPoolExecutor;
    }

    private static class UiThreadHolder {
        private static final Handler HANDLER = new Handler(Looper.getMainLooper());
        private static final HandlerExecutor HANDLER_EXECUTOR = new HandlerExecutor(HANDLER);
    }

    private static class CommonExecutorHolder {
        private static final ExecutorService EXECUTOR =
                Executors.unconfigurableExecutorService(
                        Executors.newCachedThreadPool(
                                new NamedThreadFactory("CommonPool")));
    }
}
