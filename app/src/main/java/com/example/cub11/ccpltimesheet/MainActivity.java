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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.cub11.ccpltimesheet.Utils.getCurrentTime;
import static com.example.cub11.ccpltimesheet.Utils.getFinalDate;
import static com.example.cub11.ccpltimesheet.Utils.makeTimeDiff;
import static com.example.cub11.ccpltimesheet.Utils.showAlertDialog;

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
        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "ABSENT", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Absent marked!", getApplicationContext());
    }

    public void onHolidayClicked() {
        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "HOLIDAY", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Holiday marked!", getApplicationContext());
    }

    public void onPunchInClicked() {
        String inTime = getCurrentTime();
        String outTime = "-:-:-";
        String totalTime = "";

        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), totalTime, inTime, outTime, "PUNCH_IN", (Calendar.getInstance().getTime()).getTime(), -1L));
        showAlertDialog("punch in time marked!", getApplicationContext());

    }

    public void onPunchOutClicked() {
        //match this punch out with nearest punch in time

        long diff = Long.MAX_VALUE;
        AttendanceItem targetItem = null;
        long currentMilliSeconds = (Calendar.getInstance().getTime()).getTime();
        for (AttendanceItem item : attendanceItemList) {
            if (item.getOutTime().equals("-:-:-")) {
                long tempDiff = currentMilliSeconds - item.getInMilliSeconds();
                if (tempDiff < diff) {
                    diff = tempDiff;
                    targetItem = item;
                }
            }
        }

        if (diff != Long.MAX_VALUE) {
            //update the existing record
            targetItem.setOutTime(getCurrentTime());
            targetItem.setOutMilliSeconds(currentMilliSeconds);
            String totalTime = makeTimeDiff((targetItem.getOutMilliSeconds() - targetItem.getInMilliSeconds()));
            targetItem.setTotalTime(totalTime);
        } else {
            //create a new record
            String inTime = "-:-:-";
            String outTime = getCurrentTime();
            String totalTime = "";

            attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), totalTime, inTime, outTime, "PUNCH_OUT", (Calendar.getInstance().getTime()).getTime(), -1L));
        }
        showAlertDialog("punch out time marked!", getApplicationContext());

    }


}

