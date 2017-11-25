package com.muchnik.logmonitor.service.file;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.entity.RecordWithStat;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<Record> read(File file);
    void saveInFormat(String fileName, Map<Date, List<RecordWithStat>> avgMap);
}
