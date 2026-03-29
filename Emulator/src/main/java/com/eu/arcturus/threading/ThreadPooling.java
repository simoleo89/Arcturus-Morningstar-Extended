package com.eu.arcturus.threading;

import com.eu.arcturus.Emulator;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadPooling {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPooling.class);

    public final int threads;
    private final ScheduledExecutorService scheduledPool;
    private final ExecutorService virtualThreadPool;
    private volatile boolean canAdd;

    public ThreadPooling(Integer threads) {
        this.threads = threads;
        this.scheduledPool = new HabboExecutorService(this.threads, new DefaultThreadFactory("HabExec"));
        this.virtualThreadPool = Executors.newVirtualThreadPerTaskExecutor();
        this.canAdd = true;
        LOGGER.info("Thread Pool -> Loaded! (Virtual Threads enabled)");
    }

    public ScheduledFuture<?> run(Runnable run) {
        try {
            if (this.canAdd) {
                return this.run(run, 0);
            } else {
                if (Emulator.isShuttingDown) {
                    run.run();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Caught exception", e);
        }

        return null;
    }

    public ScheduledFuture<?> run(Runnable run, long delay) {
        try {
            if (this.canAdd) {
                Runnable safeRun = () -> {
                    try {
                        run.run();
                    } catch (Exception e) {
                        LOGGER.error("Caught exception", e);
                    }
                };

                if (delay <= 0) {
                    // No delay: run directly on a virtual thread
                    this.virtualThreadPool.execute(safeRun);
                    return null;
                }

                // With delay: schedule on platform thread pool, then delegate to virtual thread
                return this.scheduledPool.schedule(() -> {
                    this.virtualThreadPool.execute(safeRun);
                }, delay, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            LOGGER.error("Caught exception", e);
        }

        return null;
    }

    public void shutDown() {
        this.canAdd = false;
        this.scheduledPool.shutdownNow();
        this.virtualThreadPool.close();

        LOGGER.info("Threading -> Disposed!");
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public ScheduledExecutorService getService() {
        return this.scheduledPool;
    }
}
