package com.muchnik.logmonitor.service.file.impl;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.entity.RecordWithStat;
import com.muchnik.logmonitor.service.file.FileService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.muchnik.logmonitor.util.ConfigLoader.OUTPUT_FOLDER_KEY;
import static com.muchnik.logmonitor.util.ConfigLoader.getProperty;

public class CsvFIleService implements FileService {
    private static final String SAVE_FILE_PREFIX = "avg_";
    private static final String SEPARATOR = ",";
    private static String csvHeaders;

    @Override
    public void saveInFormat(String fileName, Map<Date, List<RecordWithStat>> avgMap) {

        File outputFile = new File(getProperty(OUTPUT_FOLDER_KEY) + File.separator + SAVE_FILE_PREFIX + fileName);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(outputFile))) {
            for (Date key : avgMap.keySet()) {
                List<RecordWithStat> records = avgMap.get(key);
                writeDaysAndHeaders(out, key);
                records.stream().forEach(r -> writeRecord(out, r));
                out.write(System.lineSeparator());
                out.flush();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void writeRecord(BufferedWriter out, RecordWithStat rec) {
        try {
            out.write(rec.getName());
            out.write(SEPARATOR);
            out.write(rec.getSite());
            out.write(SEPARATOR);
            out.write(rec.getDuration() + "");
            out.write(System.lineSeparator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeDaysAndHeaders(BufferedWriter out, Date key) {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            out.write(formatter.format(key));
            out.write(System.lineSeparator());
            out.write(System.lineSeparator());
            out.write("userid,site,duration");
            out.write(System.lineSeparator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Record> read(File file) {
        List<Record> records = new CopyOnWriteArrayList<>();
        String line;
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            boolean isHeaders = true;
            while ((line = br.readLine()) != null) {
                if (isHeaders) {
                    csvHeaders = line;
                    isHeaders = false;
                } else {
                    String[] values = line.split(splitBy);

                    String userName = values[1];
                    long timeStamp = Long.valueOf(values[0]);
                    String siteName = values[2];
                    int duration = Integer.valueOf(values[3]);

                    Record record = new Record(userName, timeStamp, siteName, duration, file.getName());
                    records.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
