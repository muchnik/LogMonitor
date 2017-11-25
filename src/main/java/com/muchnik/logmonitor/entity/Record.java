package com.muchnik.logmonitor.entity;

public class Record {
    private String userName;
    private long timeStamp;
    private String site;
    private long duration;
    private String fileName;

    public Record(String userName, long timeStamp, String site, long duration) {
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.site = site;
        this.duration = duration;
    }

    public Record(String userName, long timeStamp, String site, long duration, String fileName) {
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.site = site;
        this.duration = duration;
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Record{" +
                "userName='" + userName + '\'' +
                ", timeStamp=" + timeStamp +
                ", site='" + site + '\'' +
                ", duration=" + duration +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (timeStamp != record.timeStamp) return false;
        if (duration != record.duration) return false;
        if (!userName.equals(record.userName)) return false;
        return site.equals(record.site);
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = 31 * result + site.hashCode();
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        return result;
    }
}
