package com.muchnik.logmonitor.persistence.impl;

import com.muchnik.logmonitor.entity.Record;
import com.muchnik.logmonitor.persistence.datasource.DataSource;
import com.muchnik.logmonitor.persistence.RecordDao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JdbcRecordDaoImpl implements RecordDao {
    private Connection con = null;

    public JdbcRecordDaoImpl() {
        try {
            this.con = DataSource.getInstance().getConnection();
            //Create needed tables if not exists
            String createTableProcessedFiles = "CREATE TABLE IF NOT EXISTS processed_files (id BIGSERIAL, filename TEXT, PRIMARY KEY (id));";
            String createRecorsTable = "CREATE TABLE IF NOT EXISTS records (id BIGSERIAL, time_stamp BIGINT, username TEXT, site TEXT, duration BIGINT, filename TEXT, PRIMARY KEY (id));";
            con.createStatement().executeUpdate(createTableProcessedFiles);
            con.createStatement().executeUpdate(createRecorsTable);
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save processed file to processed_files table to avoid duplicates in main table
     * @param fileName
     */
    private void saveProcessedFiles(String fileName) {
        String saveProcessedFileName = "INSERT INTO processed_files (filename) SELECT ? WHERE NOT EXISTS (SELECT filename from processed_files WHERE filename = ?);";
        try (PreparedStatement prstmt = con.prepareStatement(saveProcessedFileName)) {
            prstmt.setString(1, fileName);
            prstmt.setString(2, fileName);
            prstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<Record> records) {
        //language=PostgreSQL
        String sql = "INSERT INTO records (time_stamp, username, site, duration, filename) SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT filename FROM processed_files WHERE filename = ?);";

        PreparedStatement prstmt = null;

        try {
            prstmt = con.prepareStatement(sql);
            for (Record record : records) {
                prstmt.setLong(1, record.getTimeStamp());
                prstmt.setString(2, record.getUserName());
                prstmt.setString(3, record.getSite());
                prstmt.setLong(4, record.getDuration());
                prstmt.setString(5, record.getFileName());
                prstmt.setString(6, record.getFileName());
                prstmt.executeUpdate();
            }
            saveProcessedFiles(records.get(0).getFileName());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prstmt != null) {
                    prstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Record> getAllWithFileName(String fileName) {
        List<Record> records = new ArrayList<>();
        //language=PostgreSQL
        String query = "SELECT time_stamp, username, site, duration, filename  FROM records WHERE filename = '" + fileName + "';";

        try (
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
        ) {
            while (rs.next()) {
                long timeStamp = rs.getLong("time_stamp");
                String userName = rs.getString("username");
                String site = rs.getString("site");
                long duration = rs.getLong("duration");
                String filename = rs.getString("filename");
                Record record = new Record(userName, timeStamp, site, duration, filename);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Record> getAll() {
        List<Record> records = new CopyOnWriteArrayList<>();
        //language=PostgreSQL
        String preparedSql = "SELECT time_stamp, username, site, duration  FROM records;";

        try (
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(preparedSql);
        ) {
            while (rs.next()) {
                long timeStamp = rs.getLong("time_stamp");
                String userName = rs.getString("username");
                String site = rs.getString("site");
                long duration = rs.getLong("duration");
                Record record = new Record(userName, timeStamp, site, duration);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<Record> searchByUserId(String userID) {
        List<Record> records = new CopyOnWriteArrayList<>();
        //language=PostgreSQL
        String preparedSql = "SELECT time_stamp, username, site, duration  FROM records WHERE username LIKE ?;";
        PreparedStatement prstmt = null;
        ResultSet rs = null;
        try {
            prstmt = con.prepareStatement(preparedSql);
            prstmt.setString(1, "%" + userID + "%");
            rs = prstmt.executeQuery();

            while (rs.next()) {
                long timeStamp = rs.getLong("time_stamp");
                String userName = rs.getString("username");
                String site = rs.getString("site");
                long duration = rs.getLong("duration");
                Record record = new Record(userName, timeStamp, site, duration);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prstmt != null) {
                    prstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return records;
    }
}

