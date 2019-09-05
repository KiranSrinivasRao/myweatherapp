package com.fireflies.myweather.executers;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executor is an object that executes submitted Runnable tasks
 */
public class AppExecutors {

    // For Singleton Instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getsInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        // diskIO is a single thread executor so our database is not in race condition
                        Executors.newFixedThreadPool(3),
                        // networkIO is a thread pool of 3 threads - different network calls simultaneously
                        new MainThreadExecutor());
                // when we are in activity we don't need this MainThread Executor - runOnUiThread is present

                // when we are in different class and don't have access to runOnUIThread,
                // then this MainThreadExecutor can be used
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
