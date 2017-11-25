package com.muchnik.logmonitor.service.record;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.entity.RecordWithStat;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RecordService {
    void save(List<Record> records);
    List<Record> searchByUserId(String userId);
    Map<Date, List<RecordWithStat>> getAvgRecords(String fileName);
    List<Record> getAll();
    void sort(Map<Date, List<RecordWithStat>> map);
}
