package com.example.cub11.ccpltimesheet.database.model;

import android.content.ContentValues;

public class AttendanceItem {

    public static final String TABLE = "attendance_item_table";

    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String TOTAL_TIME = "total_time";
    public static final String IN_TIME = "in_time";
    public static final String OUT_TIME = "out_time";
    public static final String TYPE = "type";

    private long id;
    private String date;
    private String totalTime;
    private String inTime;
    private String outTime;
    private String type;


    public AttendanceItem(long id, String date, String totalTime, String inTime, String outTime, String type) {
        this.id = id;
        this.date = date;
        this.totalTime = totalTime;
        this.inTime = inTime;
        this.outTime = outTime;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder date(String date) {
            values.put(DATE, date);
            return this;
        }

        public Builder inTime(String inTime) {
            values.put(IN_TIME, inTime);
            return this;
        }

        public Builder type(String type) {
            values.put(TYPE, type);
            return this;
        }

        public Builder totalTime(String totalTime) {
            values.put(TOTAL_TIME, totalTime);
            return this;
        }

        public Builder outTime(String outTime) {
            values.put(OUT_TIME, outTime);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
