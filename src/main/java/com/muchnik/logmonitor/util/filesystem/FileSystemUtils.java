package com.muchnik.logmonitor.util.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.muchnik.logmonitor.util.ConfigLoader.*;

public class FileSystemUtils {
    public static List<File> getFiles() {
        File workDirectory = new File(getProperty(INPUT_FOLDER_KEY));
        File[] listOfFiles = workDirectory.listFiles();
        List<File> filesInInputFolder = new ArrayList<>(100);

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    if (!filesInInputFolder.contains(file)) {
                        filesInInputFolder.add(file);
                    }
                }
            }
        }
        return filesInInputFolder;
    }
}
