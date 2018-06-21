package com.example.cub11.ccpltimesheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cub11.ccpltimesheet.database.DbOpenHelper;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;
import com.example.cub11.ccpltimesheet.view.BookmarkFragment;
import com.example.cub11.ccpltimesheet.view.DatePickerFragment;
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

    static int listIndex = 1;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        attendanceItemList = new ArrayList<>();
        final ImageView button = (ImageView) findViewById(R.id.toolbarbtn);
        final TextView tabtext = (TextView) findViewById(R.id.tabText);
        final ImageView backButton = (ImageView) findViewById(R.id.backButtton);

        tabtext.setText("");


        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        backButton.setEnabled(false);
        backButton.setVisibility(View.INVISIBLE);

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
                            transaction.addToBackStack("fragmentbookmark");
                            transaction.commit();
                            selectedFragment = "Bookmark";
                            button.setEnabled(false);
                            button.setVisibility(View.INVISIBLE);
                            tabtext.setVisibility(View.INVISIBLE);
                            backButton.setVisibility(View.INVISIBLE);
                            backButton.setEnabled(false);


                        }
                        break;
                    }
                    case R.id.action_history: {
                        if (!selectedFragment.equalsIgnoreCase("History")) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, HistoryFragment.newInstance());
                            transaction.commit();
                            selectedFragment = "History";
                            button.setEnabled(true);
                            button.setVisibility(View.VISIBLE);
                            tabtext.setVisibility(View.VISIBLE);
                            tabtext.setText("Timeline");
                            backButton.setVisibility(View.INVISIBLE);
                            backButton.setEnabled(false);
                        }
                        break;
                    }
                }

                return true;
            }
        });


        db = new DbOpenHelper(this);
        attendanceItemList = db.getAllAttendanceItems();

//        Log.e("raman", "list " + attendanceItemList.size());
//        if (attendanceItemList.isEmpty()) {
//            attendanceItemList.add(new AttendanceItem(1, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", "ABSENT", 2344213, 132233));
//            attendanceItemList.add(new AttendanceItem(2, "12-25-2014", "12-25-2014", "09:00", "12:02:12", "08:22:12", "HOLIDAY", 234421233, 3423123));
//        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, DatePickerFragment.newInstance());
                transaction.addToBackStack("FragmentB");
                transaction.commit();

            }
        });

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
        attendanceItemList.add(new AttendanceItem(listIndex++, getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "ABSENT", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Absent marked!");
    }

    private String getFinalDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("hh:mm:ss");
        String localTime = date.format(currentTime);


        long milliSeconds = currentTime.getTime();


        DateFormat yearOnly = new SimpleDateFormat("yyyy");
        String yearValue = yearOnly.format(currentTime);
        int dateValue = currentTime.getDate();

        int monthIndex = currentTime.getMonth();
        String monthName = "";
        switch (monthIndex) {
            case 0:
                monthName = "Jan";
                break;
            case 1:
                monthName = "Feb";
                break;
            case 2:
                monthName = "Mar";
                break;
            case 3:
                monthName = "Apr";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "Jun";
                break;
            case 6:
                monthName = "Jul";
                break;
            case 7:
                monthName = "Aug";
                break;
            case 8:
                monthName = "Sep";
                break;
            case 9:
                monthName = "Oct";
                break;
            case 10:
                monthName = "Nov";
                break;
            case 11:
                monthName = "Dec";
                break;

        }


        int dayIndex = currentTime.getDay();
        String dayName = "";
        switch (dayIndex) {
            case 0:
                dayName = "Sunday";
                break;
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;
        }

        String finalDate = monthName + " " + dateValue + " " + yearValue + " , " + dayName;
        Log.e("raman", "finalDate " + finalDate + " and local time is :" + localTime + " and time in milliseconds is : " + milliSeconds);


        return finalDate;
    }


    public void onHolidayClicked() {
        attendanceItemList.add(new AttendanceItem(listIndex++, getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "HOLIDAY", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Holiday marked!");
    }

    public void onPunchInClicked() {
        String inTime = getCurrentTime();
        String outTime = "-:-:-";
        String totalTime = "";

        attendanceItemList.add(new AttendanceItem(listIndex++, getFinalDate(), getFinalDate(), totalTime, inTime, outTime, "PUNCH_IN", (Calendar.getInstance().getTime()).getTime(), -1L));
        showAlertDialog("punch in time marked!");

    }

    private String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("hh:mm:ss a");
        String localTime = date.format(currentTime);
        return localTime;
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
            String totalTime = String.valueOf((targetItem.getOutMilliSeconds() - targetItem.getInMilliSeconds()) / (1000 * 3600));
            targetItem.setTotalTime(totalTime);
        } else {
            //create a new record
            String inTime = "-:-:-";
            String outTime = getCurrentTime();
            String totalTime = "";

            attendanceItemList.add(new AttendanceItem(listIndex++, getFinalDate(), getFinalDate(), totalTime, inTime, outTime, "PUNCH_IN", (Calendar.getInstance().getTime()).getTime(), -1L));
        }
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}

