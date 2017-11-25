package com.muchnik.logmonitor.entity;

import java.util.Date;

public class RecordWithStat implements Comparable {
    private String name;
    private long duration;
    private String site;
    private Date date;

    public RecordWithStat(String userName, String site, Date date, long avgDuration) {
        this.name = userName;
        this.site = site;
        this.duration = avgDuration;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Object that) {
        if (that instanceof RecordWithStat) {
            return (this.getName().compareTo(((RecordWithStat) that).getName()));
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordWithStat that = (RecordWithStat) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return site != null ? site.equals(that.site) : that.site == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (site != null ? site.hashCode() : 0);
        return result;
    }
}
