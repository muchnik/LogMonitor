package com.muchnik.logmonitor.persistence;

import com.muchnik.logmonitor.entity.Record;

import java.util.List;

public interface RecordDao {
    void saveAll(List<Record> records);
    List<Record> getAll();
    List<Record> searchByUserId(String userID);
    List<Record> getAllWithFileName(String fileName);
}
