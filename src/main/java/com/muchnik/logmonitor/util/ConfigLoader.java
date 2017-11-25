package com.muchnik.logmonitor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    public static final String INPUT_FOLDER_KEY = "inputFolder";
    public static final String OUTPUT_FOLDER_KEY = "outputFolder";
    public static final String DB_USER_NAME_KEY = "dbUserName";
    public static final String DB_PASSWORD_KEY = "dbPassword";
    public static final String DB_URL_KEY = "dbUrl";

    static {
        File file = new File(ConfigLoader.class.getClassLoader().getResource("config.properties").getFile());

        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
