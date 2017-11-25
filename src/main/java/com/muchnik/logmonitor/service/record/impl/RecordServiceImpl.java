package com.muchnik.logmonitor.service.record.impl;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.entity.RecordWithStat;
import com.muchnik.logmonitor.persistence.RecordDao;
import com.muchnik.logmonitor.persistence.impl.JdbcRecordDaoImpl;
import com.muchnik.logmonitor.service.record.RecordService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class RecordServiceImpl implements RecordService {
    private static List<String> savedFiles = Collections.synchronizedList(new ArrayList<>());
    private RecordDao repository = new JdbcRecordDaoImpl();

    @Override
    public void save(List<Record> records) {
        //check if we already processed file, before go to database.
        boolean saved = savedFiles.contains(records.get(0).getFileName());
        if (!saved) {
            repository.saveAll(records);
            savedFiles.add(records.get(0).getFileName());
        }
    }

    @Override
    public List<Record> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Record> searchByUserId(String userId) {
        return repository.searchByUserId(userId);
    }

    @Override
    public Map<Date, List<RecordWithStat>> getAvgRecords(String fileName) {
        List<Record> records = repository.getAllWithFileName(fileName);
        Map<Tuple, List<Record>> collect = records.stream()
                .collect(Collectors.groupingBy(r -> Tuple.of(r.getUserName(), r.getSite(), getDay(r))));

        List<RecordWithStat> recordWithStats = new ArrayList<>();
        for (Tuple tuple : collect.keySet()) {
            long avgDuration = (long) collect.get(tuple).stream().mapToLong(Record::getDuration).average().orElse(0);
            RecordWithStat recordWithStat = new RecordWithStat(tuple.getUserName(), tuple.getUrl(), tuple.getDate(), avgDuration );
            recordWithStats.add(recordWithStat);
        }

        return recordWithStats.stream().collect(Collectors.groupingBy(RecordWithStat::getDate));
    }

    private static Date getDay(Record record){
        long timeStamp = record.getTimeStamp();
        LocalDateTime date = LocalDateTime.ofInstant(new Date(timeStamp * 1000L).toInstant(), ZoneId.systemDefault());
        return Date.from(date.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void sort(Map<Date, List<RecordWithStat>> map) {
        Collection<List<RecordWithStat>> records = map.values();

        for (List<RecordWithStat> r : records) {
            Collections.sort(r, new Comparator<RecordWithStat>() {
                public int compare(RecordWithStat o1, RecordWithStat o2) {
                    return removeInt(o1) - removeInt(o2);
                }

                int removeInt(RecordWithStat record) {
                    String num = record.getName().replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            });
        }
    }

    private static class Tuple {
        private final String userName;
        private final String url;
        private final Date date;

        public static Tuple of(String userName, String url, Date date) {
            return new Tuple(userName, url, date);
        }


        private Tuple(String userName, String url, Date date) {
            this.userName = userName;
            this.url = url;
            this.date = date;
        }

        public String getUserName() {
            return userName;
        }

        public String getUrl() {
            return url;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tuple tuple = (Tuple) o;

            if (!userName.equals(tuple.userName)) return false;
            if (!url.equals(tuple.url)) return false;
            return date.equals(tuple.date);
        }

        @Override
        public int hashCode() {
            int result = userName.hashCode();
            result = 31 * result + url.hashCode();
            result = 31 * result + date.hashCode();
            return result;
        }
    }
}
