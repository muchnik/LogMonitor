package com.muchnik.logmonitor.util.filesystem;

import com.muchnik.logmonitor.LogMonitorApplication;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static com.muchnik.logmonitor.util.ConfigLoader.INPUT_FOLDER_KEY;
import static com.muchnik.logmonitor.util.ConfigLoader.getProperty;

public class DirectoryWatcher implements Runnable {
    @Override
    public void run() {
        WatchService watchService;
        Path path = Paths.get(getProperty(INPUT_FOLDER_KEY));
        WatchKey key;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    LogMonitorApplication.getInitializer().addFilesToProceed(event.context().toString());
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
