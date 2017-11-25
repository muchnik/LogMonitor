package com.muchnik.logmonitor.core;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.entity.RecordWithStat;
import com.muchnik.logmonitor.service.record.RecordService;
import com.muchnik.logmonitor.service.record.impl.RecordServiceImpl;
import com.muchnik.logmonitor.service.file.impl.CsvFIleService;
import com.muchnik.logmonitor.service.file.FileService;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable {
    private final File fileToParse;
    private FileService fileService = new CsvFIleService();
    private RecordService recordService = new RecordServiceImpl();

    public Worker(File fileToParse) {
        this.fileToParse = fileToParse;
    }

    @Override
    public void run() {
        List<Record> records = fileService.read(fileToParse);
        recordService.save(records);
        Map<Date, List<RecordWithStat>> avgRecords = recordService.getAvgRecords(fileToParse.getName());
        recordService.sort(avgRecords);
        fileService.saveInFormat(fileToParse.getName(), avgRecords);
    }
}