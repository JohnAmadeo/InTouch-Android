/*
 * Adapted from Eyal Scheneider's answer in
 * https://stackoverflow.com/questions/4742210/implementing-debounce-in-java
 */

package com.example.android.intouch_android.utils;

import android.arch.lifecycle.Transformations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: We probably don't need [delayedMap] and can just use a single TimerTask variable, but that would
// require handling concurrency issues and it's easier for now to just use the ConcurrentHashMap
// provided in the StackOverflow answer's implementation
public class Debouncer <T> {
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<T, TimerTask> delayedMap = new ConcurrentHashMap<T, TimerTask>();
    private final Callback callback;
    private final int interval;

    public interface Callback {
        public void call();
    }

    public Debouncer(Callback c, int interval) {
        this.callback = c;
        this.interval = interval;
    }

    public void call(T key) {
        TimerTask task = new TimerTask(key);

        TimerTask prevTask;
        do {
            // prevTask = task2
            prevTask = delayedMap.putIfAbsent(key, task);
            if (prevTask == null)
                scheduledExecutor.schedule(task, interval, TimeUnit.MILLISECONDS);
        }
        while (prevTask != null && !prevTask.extend()); // Exit only if new task was added to map, or existing task was extended successfully
    }

    public void terminate() {
        scheduledExecutor.shutdownNow();
    }

    // The task that wakes up when the wait time elapses
    private class TimerTask implements Runnable {
        private final T key;
        private long dueTime;
        private final Object lock = new Object();

        public TimerTask(T key) {
            this.key = key;
            extend();
        }

        public boolean extend() {
            synchronized (lock) {
                if (dueTime < 0) // Task has been shutdown
                    return false;
                dueTime = System.currentTimeMillis() + interval;
                return true;
            }
        }

        public void run() {
            synchronized (lock) {
                long remaining = dueTime - System.currentTimeMillis();
                if (remaining > 0) { // Re-schedule task
                    scheduledExecutor.schedule(this, remaining, TimeUnit.MILLISECONDS);
                } else { // Mark as terminated and invoke callback
                    dueTime = -1;
                    try {
                        callback.call();
                    } finally {
                        delayedMap.remove(key);
                    }
                }
            }
        }
    }
}