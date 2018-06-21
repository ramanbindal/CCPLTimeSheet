package com.example.cub11.ccpltimesheet.view;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.cub11.ccpltimesheet.Const;
import com.example.cub11.ccpltimesheet.MainActivity;
import com.example.cub11.ccpltimesheet.R;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.cub11.ccpltimesheet.Utils.findAmorPm;
import static com.example.cub11.ccpltimesheet.Utils.makeTime;
import static com.example.cub11.ccpltimesheet.Utils.makeTimeDiff;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment {

    private static final int COLOR_BLUE = Color.parseColor("#86BFFF");
    DatePicker datePicker;
    TimePicker timePicker;
    EditText timeInEdit;
    EditText timeOutEdit;
    Button doneBtn;
    String state = Const.WORKING_DAY;
    String amPMTime = "";
    String finalTimeInStr = "";
    String finalTimeOutStr = "";
    long inTimeInMillis;
    long outTimeInMillis;

    private static final int COLOR_WHITE = Color.parseColor("#ffffff");

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepickerfragment_layout, container, false);
        // Inflate the layout for this fragment


        final Button working = (Button) view.findViewById(R.id.working);
        final Button absent = (Button) view.findViewById(R.id.absent);
        final Button holiday = (Button) view.findViewById(R.id.holiday);

        doneBtn = view.findViewById(R.id.doneButton);
        working.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackground) );
        holiday.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
        absent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
        working.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = Const.WORKING_DAY;
                working.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackground) );
                holiday.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
                absent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = Const.ABSENT;
                working.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
                holiday.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
                absent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackground) );
            }
        });
        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                working.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
                holiday.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackground) );
                absent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.buttonbackgroundborder) );
                state = Const.HOLIDAY;

            }
        });

        //working.setBackgroundColor(COLOR_BLUE);
        timeInEdit = (EditText) view.findViewById(R.id.timeInEditText);
        timeOutEdit = (EditText) view.findViewById(R.id.timeOutEditText);
        timeInEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("TimeIn");
            }
        });
        timeOutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("TimeOut");
            }
        });
        timeInEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    showDialog("TimeIn");
                }
            }
        });
        timeOutEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    showDialog("TimeOut");
                }
            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
            }
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbarVisibilityManager(Const.DatePickerFragment);

        return view;
    }

    private void onDoneClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        AttendanceItem attendanceItem = new AttendanceItem();

        if (state.equalsIgnoreCase(Const.WORKING_DAY)) {
            if (timeInEdit.getText().toString().isEmpty()) {
                showAlertDialog("Please select Punch In Time !", false);
            } else if (timeOutEdit.getText().toString().isEmpty()) {
                showAlertDialog("Please select Punch Out Time !", false);
            } else {
                String[] tempStrIn = finalTimeInStr.split(", ");
                String[] tempStrOut = finalTimeOutStr.split(", ");
                long inOutTimeDiff = Math.abs(outTimeInMillis - inTimeInMillis);
                mainActivity.getAttendanceItemList().add(new AttendanceItem(tempStrIn[0], tempStrOut[0], makeTimeDiff(inOutTimeDiff), tempStrIn[1],
                        tempStrOut[1], Const.WORKING_DAY, inTimeInMillis, outTimeInMillis));
                showAlertDialog("Attendance Marked!", true);
            }
        } else if (state.equalsIgnoreCase(Const.ABSENT)) {
            if (timeInEdit.getText().toString().isEmpty()) {
                showAlertDialog("Please select Date to mark Absent !", false);
            } else {

                String[] tempStrIn = finalTimeInStr.split(", ");
                mainActivity.getAttendanceItemList().add(new AttendanceItem(tempStrIn[0], tempStrIn[0], Const.DefaultTotalTime, Const.DefaultPunchInTime, Const.DefaultPunchOutTime, Const.ABSENT, (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
                showAlertDialog("Absent marked!", true);
            }
        } else if (state.equalsIgnoreCase(Const.HOLIDAY)) {

            if (timeInEdit.getText().toString().isEmpty()) {
                showAlertDialog("Please select Date to mark Holiday !", false);
            } else {

                String[] tempStrIn = finalTimeInStr.split(", ");
                mainActivity.getAttendanceItemList().add(new AttendanceItem(tempStrIn[0], tempStrIn[0], Const.DefaultTotalTime, Const.DefaultPunchInTime, Const.DefaultPunchOutTime, Const.HOLIDAY, (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
                showAlertDialog("Holiday marked!", true);
            }
        }
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public void showDialog(final String time) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.time_picker_dialog);
        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);

        final Button submit = (Button) dialog.findViewById(R.id.submitButton);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                amPMTime = makeTime(hour, minute);

                String amOrPm = findAmorPm(hour, minute);

                String timeIn = "";
                String timeOut = "";

                if (time.equals("TimeIn")) {
                    timeIn = day + "/" + month + "/" + year + " " + hour + ":" + minute + ":00 " + amOrPm;
                    String str = getFinalString(timeIn, 1);
                    finalTimeInStr = str;
                    timeInEdit.setText(str);
                    dialog.dismiss();

                }
                if (time.equalsIgnoreCase("TimeOut")) {

                    timeOut = day + "/" + month + "/" + year + " " + " " + hour + ":" + minute + ":00 " + amOrPm;
                    String str = getFinalString(timeOut, 2);
                    finalTimeOutStr = str;
                    timeOutEdit.setText(str);
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    public String getFinalString(String time, int flag) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String str = "";

        try {
            date = simpleDateFormat.parse(time);
            if (flag == 1) {
                inTimeInMillis = date.getTime();
            } else {
                outTimeInMillis = date.getTime();
            }
            String[] strArray = date.toString().split(" ");
            String day = strArray[0];
            String fullDayName = getFullDayName(day);
            str = strArray[1] + " " + strArray[2] + " " + strArray[5] + "," + fullDayName + ", " + amPMTime;
            Log.e("Harsh", date.toString() + " " + date.getTime() + "final String " + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    private String getFullDayName(String day) {
        switch (day) {
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            case "Sat":
                return "Saturday";
            case "Sun":
                return "Sunday";
        }
        return "";
    }

    public void showAlertDialog(String message, final Boolean toCloseFragment) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                if (toCloseFragment) {
                    dialog.dismiss();
                    onBackPressed();
                } else {
                    dialog.dismiss();

                }
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
