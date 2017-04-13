package com.teaminology.hp.Enum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum ImportTMXExecutor
{
    INSTANCE;

    ExecutorService executor;

    private ImportTMXExecutor() {
        executor = Executors.newFixedThreadPool(10);
    }

    public ExecutorService get() {
        return executor;
    }
}
