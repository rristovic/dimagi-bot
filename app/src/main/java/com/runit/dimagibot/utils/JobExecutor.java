package com.runit.dimagibot.utils;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Helper class for executing tasks in background and main thread.
 */
public class JobExecutor implements Executor {
    private static final int NUM_OF_THREADS = 5;
    private static ExecutorService executorService;
    private static ScheduledExecutorService scheduledExecutorService;

    static {
        executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }

    @Override
    public void executeOnMain(Runnable command) {
        final android.os.Handler UIHandler = new android.os.Handler(Looper.getMainLooper());
        UIHandler.post(command);
    }
}
