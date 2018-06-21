package com.example.cub11.ccpltimesheet;

import android.content.Context;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.cub11.ccpltimesheet.Utils.getCurrentTime;
import static com.example.cub11.ccpltimesheet.Utils.getFinalDate;
import static com.example.cub11.ccpltimesheet.Utils.makeTimeDiff;

public class MainActivity extends AppCompatActivity {
    private DbOpenHelper db;
    private String selectedFragment = "";
    private List<AttendanceItem> attendanceItemList;
    private ImageView frwdButton, backButton;
    private TextView tabtext;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        attendanceItemList = new ArrayList<>();
        frwdButton = (ImageView) findViewById(R.id.toolbarbtn);
        tabtext = (TextView) findViewById(R.id.tabText);
        backButton = (ImageView) findViewById(R.id.backButtton);

        toolbarVisibilityManager(Const.BookmarkFragment);


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, BookmarkFragment.newInstance());
        transaction.commit();
        selectedFragment = Const.BookmarkFragment;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bookmarks: {
                        if (!selectedFragment.equalsIgnoreCase(Const.BookmarkFragment)) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, BookmarkFragment.newInstance());
                            transaction.commit();
                            selectedFragment = Const.BookmarkFragment;
                            toolbarVisibilityManager(Const.BookmarkFragment);
                        }
                        break;
                    }
                    case R.id.action_history: {
                        if (!selectedFragment.equalsIgnoreCase(Const.HistoryFragment)) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, HistoryFragment.newInstance());
                            transaction.commit();
                            selectedFragment = Const.HistoryFragment;
                            toolbarVisibilityManager(Const.HistoryFragment);
                        }
                        break;
                    }
                }

                return true;
            }
        });


        db = new DbOpenHelper(this);
        attendanceItemList = db.getAllAttendanceItems();


        frwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, DatePickerFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

    }

    public void toolbarVisibilityManager(String fragmentName) {
        switch (fragmentName) {
            case Const.BookmarkFragment:
                frwdButton.setEnabled(false);
                backButton.setEnabled(false);
                tabtext.setText("Mark Your Attendance");
                tabtext.setVisibility(View.VISIBLE);
                frwdButton.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                break;

            case Const.HistoryFragment:
                frwdButton.setEnabled(true);
                frwdButton.setVisibility(View.VISIBLE);
                tabtext.setVisibility(View.VISIBLE);
                tabtext.setText("Timeline");
                backButton.setVisibility(View.INVISIBLE);
                backButton.setEnabled(false);
                break;
            case Const.DatePickerFragment:
                frwdButton.setEnabled(false);
                frwdButton.setVisibility(View.INVISIBLE);
                tabtext.setVisibility(View.VISIBLE);
                tabtext.setText("Add Item");
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    public List<AttendanceItem> getAttendanceItemList() {
        return attendanceItemList;
    }


    public void onAbsentClicked() {
        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), Const.DefaultTotalTime, Const.DefaultPunchInTime, Const.DefaultPunchOutTime, Const.ABSENT, (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Absent marked!", getApplicationContext());
    }

    public void onHolidayClicked() {
        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), Const.DefaultTotalTime, Const.DefaultPunchInTime, Const.DefaultPunchOutTime, Const.HOLIDAY, (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
        showAlertDialog("Holiday marked!", getApplicationContext());
    }

    public void onPunchInClicked() {
        String inTime = getCurrentTime();
        String outTime = "-:-:-";
        String totalTime = "";

        attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), totalTime, inTime, outTime, Const.PUNCH_IN, (Calendar.getInstance().getTime()).getTime(), -1L));
        showAlertDialog("Punch In time marked!", getApplicationContext());

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

            attendanceItemList.add(new AttendanceItem(getFinalDate(), getFinalDate(), totalTime, inTime, outTime, Const.PUNCH_OUT, (Calendar.getInstance().getTime()).getTime(), -1L));
        }
        showAlertDialog("Punch out time marked!", getApplicationContext());

    }

    public void showAlertDialog(String message, Context context) {
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
    protected void onStop() {
        db.deleteAllRecords();
        db.insertAttendanceItemList(attendanceItemList);
        super.onStop();
    }
}

