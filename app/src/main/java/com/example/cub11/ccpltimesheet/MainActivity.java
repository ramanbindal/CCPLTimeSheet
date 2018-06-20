package com.example.cub11.ccpltimesheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.cub11.ccpltimesheet.database.DbOpenHelper;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;
import com.example.cub11.ccpltimesheet.view.BookmarkFragment;
import com.example.cub11.ccpltimesheet.view.HistoryFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DbOpenHelper db;
    private String selectedFragment = "";
    private List<AttendanceItem> attendanceItemList;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attendanceItemList = new ArrayList<>();

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, BookmarkFragment.newInstance());
        transaction.commit();
        selectedFragment = "Bookmark";

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bookmarks: {
                        if (!selectedFragment.equalsIgnoreCase("Bookmark")) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, BookmarkFragment.newInstance());
                            transaction.commit();
                            selectedFragment = "Bookmark";

                        }
                        break;
                    }
                    case R.id.action_history: {
                        if (!selectedFragment.equalsIgnoreCase("History")) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, HistoryFragment.newInstance());
                            transaction.commit();
                            selectedFragment = "History";
                        }
                        break;
                    }
                }

                return true;
            }
        });


        db = new DbOpenHelper(this);
        attendanceItemList = db.getAllAttendanceItems();

        Log.e("raman", "list " + attendanceItemList.size());
        if (attendanceItemList.isEmpty()) {
            attendanceItemList.add(new AttendanceItem(1, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", "ABSENT"));
            attendanceItemList.add(new AttendanceItem(2, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", "HOLIDAY"));
            attendanceItemList.add(new AttendanceItem(3, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", "dffdw"));
            attendanceItemList.add(new AttendanceItem(4, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", ""));
        }

    }

    public List<AttendanceItem> getAttendanceItemList() {
        return attendanceItemList;
    }

    public void setAttendanceItemList(List<AttendanceItem> attendanceItemList) {
        this.attendanceItemList = attendanceItemList;
    }


    @Override
    protected void onStop() {
        db.deleteAllRecords();
        db.insertAttendanceItemList(attendanceItemList);
        Log.e("raman", "list destroy " + db.getAllAttendanceItems());
        super.onStop();
    }

    public void onAbsentClicked() {

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String localTime = date.format(currentTime);


        Log.e("raman", "time " + localTime);


//        long yourmilliseconds = System.currentTimeMillis();
//        Date resultdate = new Date(yourmilliseconds);
//        Log.e("raman", "time " + resultdate);

        showAlertDialog("Absent marked!");


    }

    public void onHolidayClicked() {
        Date currentTime = Calendar.getInstance().getTime();
        showAlertDialog("Holiday marked!");


    }

    public void onPunchInClicked() {
        Date currentTime = Calendar.getInstance().getTime();
        showAlertDialog("punch in time marked!");

    }

    public void onPunchOutClicked() {
        Date currentTime = Calendar.getInstance().getTime();
        showAlertDialog("punch out time marked!");

    }


    public void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

