package com.muchnik.logmonitor;

import com.muchnik.logmonitor.core.Initializer;

public class LogMonitorApplication implements Runnable {
    private static Initializer initializer;
    @Override
    public void run() {
        initializer = new Initializer();
        initializer.start();
    }

    public static Initializer getInitializer() {
        return initializer;
    }
}
