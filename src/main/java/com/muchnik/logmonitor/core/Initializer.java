package com.muchnik.logmonitor.core;

import com.muchnik.logmonitor.util.filesystem.DirectoryWatcher;
import com.muchnik.logmonitor.util.filesystem.FileSystemUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

import static com.muchnik.logmonitor.util.ConfigLoader.*;

public class Initializer {
    private final static int MAX_THREADS = 10;
    private ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
    private LinkedBlockingQueue<File> filesToProcessQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = true;

    public void start() {
        loadDirectoryFilesOnInit();
        startAndInitDirectoryWatcher();
        listenQueue();
    }

    public void interrupt() {
        pool.shutdown();
        isRunning = false;
        Thread.currentThread().interrupt();
    }

    public void addFilesToProceed(String fileName) {
        filesToProcessQueue.add(new File(getProperty(INPUT_FOLDER_KEY) + "\\" + fileName));
    }

    private void listenQueue() {
        while (isRunning) {
            try {
                File file = filesToProcessQueue.take();
                pool.execute(new Worker(file));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startAndInitDirectoryWatcher() {
        Thread directoryWatcher = new Thread(new DirectoryWatcher());
        directoryWatcher.setDaemon(true);
        directoryWatcher.start();
    }

    private void loadDirectoryFilesOnInit() {
        List<File> listOfAllFiles = FileSystemUtils.getFiles();
        filesToProcessQueue.addAll(listOfAllFiles);
    }
}
