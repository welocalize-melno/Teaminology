package com.teaminology.hp.service.enums;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum ExportGlobalSightExecutor
{
    INSTANCE;

    ExecutorService executor;

    private ExportGlobalSightExecutor() {
        executor = Executors.newFixedThreadPool(Integer.parseInt(TeaminologyProperty.IMPORT_EXPORT_GS_JETTY_THREADS.getValue()));
    }

    public ExecutorService get() {
        return executor;
    }
}
