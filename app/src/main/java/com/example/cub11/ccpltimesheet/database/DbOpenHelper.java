package com.example.cub11.ccpltimesheet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import java.util.ArrayList;
import java.util.List;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;


    public DbOpenHelper(Context context) {
        super(context, "CCPL_TIME_SHEET.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAttendanceItemDb());
    }

    private String createAttendanceItemDb() {

        return "CREATE TABLE " + AttendanceItem.TABLE + " (" +
                AttendanceItem.ID + " LONG PRIMARY KEY AUTOINCREMENT," +
                AttendanceItem.IN_TIME + " TEXT  NULL, " +
                AttendanceItem.OUT_TIME + " TEXT  NULL, " +
                AttendanceItem.TYPE + " TEXT  NULL, " +
                AttendanceItem.IN_DATE + " TEXT NULL, " +
                AttendanceItem.OUT_DATE + " TEXT NULL, " +
                AttendanceItem.TOTAL_TIME + " TEXT NULL " +
                " );";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AttendanceItem.TABLE);
        // Create tables again
        onCreate(db);
    }

    public long insertAttendanceItem(AttendanceItem attendanceItem) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues attendanceBuider = new AttendanceItem.Builder()
                .outDate(attendanceItem.getOutDate())
                .inDate(attendanceItem.getInDate())
                .inTime(attendanceItem.getInTime())
                .outTime(attendanceItem.getOutTime())
                .totalTime(attendanceItem.getTotalTime())
                .type(attendanceItem.getType())
                .build();
        long id = db.insert(AttendanceItem.TABLE, null, attendanceBuider);

        db.close();
        return id;
    }

    public void insertAttendanceItemList(List<AttendanceItem> attendanceItemList) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        for (AttendanceItem attendanceItem : attendanceItemList) {

            ContentValues attendanceBuider = new AttendanceItem.Builder()
                    .outDate(attendanceItem.getOutDate())
                    .inDate(attendanceItem.getInDate())
                    .inTime(attendanceItem.getInTime())
                    .outTime(attendanceItem.getOutTime())
                    .totalTime(attendanceItem.getTotalTime())
                    .type(attendanceItem.getType())
                    .build();
            db.insert(AttendanceItem.TABLE, null, attendanceBuider);
        }
        db.close();
    }


//    public AttendanceItem getAttendanceItem(long id) {
//        // get readable database as we are not inserting anything
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(Note.TABLE_NAME,
//                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
//                Note.COLUMN_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // prepare note object
//        Note note = new Note(
//                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
//                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
//                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.COLUMN_TIMESTAMP)));
//
//        // close the db connection
//        cursor.close();
//
//        return note;
//    }

    public List<AttendanceItem> getAllAttendanceItems() {
        List<AttendanceItem> attendanceItems = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AttendanceItem.TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                long millis = cursor.getInt(cursor.getColumnIndex(AttendanceItem.MILLISECONDS));
                String inDate = cursor.getString(cursor.getColumnIndex(AttendanceItem.IN_DATE));
                String outDate = cursor.getString(cursor.getColumnIndex(AttendanceItem.OUT_DATE));

                String totalTime = cursor.getString(cursor.getColumnIndex(AttendanceItem.TOTAL_TIME));
                String inTime = cursor.getString(cursor.getColumnIndex(AttendanceItem.IN_TIME));
                String outTime = cursor.getString(cursor.getColumnIndex(AttendanceItem.OUT_TIME));
                String type = cursor.getString(cursor.getColumnIndex(AttendanceItem.TYPE));


                attendanceItems.add(new AttendanceItem(inDate, outDate, totalTime, inTime, outTime, type,millis));
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return attendanceItems;
    }

    public int getAttendanceItemCount() {
        String countQuery = "SELECT  * FROM " + AttendanceItem.TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }


    public void deleteAttendanceItem(AttendanceItem note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceItem.TABLE, AttendanceItem.ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + AttendanceItem.TABLE);
    }

}
