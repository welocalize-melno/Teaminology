package com.teaminology.hp.service.enums;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum ExportTMXExecutor
{
    INSTANCE;

    ExecutorService executor;

    private ExportTMXExecutor() {
        executor = Executors.newFixedThreadPool(Integer.parseInt(TeaminologyProperty.IMPORT_EXPORT_JETTY_THREADS.getValue()));
    }

    public ExecutorService get() {
        return executor;
    }
}
