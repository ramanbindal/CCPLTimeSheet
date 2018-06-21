package com.example.cub11.ccpltimesheet.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.cub11.ccpltimesheet.MainActivity;
import com.example.cub11.ccpltimesheet.R;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    String state = "working";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepickerfragment_layout, container, false);
        // Inflate the layout for this fragment


        final Button working = (Button) view.findViewById(R.id.working);
        final Button absent = (Button) view.findViewById(R.id.absent);
        final Button holiday = (Button) view.findViewById(R.id.holiday);
        doneBtn = view.findViewById(R.id.doneButton);
        working.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "working";
                working.setBackgroundColor(COLOR_BLUE);
                holiday.setBackgroundColor(COLOR_WHITE);
                absent.setBackgroundColor(COLOR_WHITE);
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "absent";
                absent.setBackgroundColor(COLOR_BLUE);
                working.setBackgroundColor(COLOR_WHITE);
                holiday.setBackgroundColor(COLOR_WHITE);
            }
        });
        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "holiday";
                holiday.setBackgroundColor(COLOR_BLUE);
                absent.setBackgroundColor(COLOR_WHITE);
                working.setBackgroundColor(COLOR_WHITE);

            }
        });

        working.setBackgroundColor(COLOR_BLUE);
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

        return view;
    }

    private void onDoneClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        AttendanceItem attendanceItem = new AttendanceItem();

        if (state.equalsIgnoreCase("working")) {
            if (timeInEdit.getText().toString().isEmpty() || timeOutEdit.getText().toString().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Please Select Time !")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {

                String[] tempStrIn = finalTimeInStr.split(", ");
                String[] tempStrOut = finalTimeOutStr.split(", ");
                long inOutTimeDiff = Math.abs(outTimeInMillis - inTimeInMillis);
                mainActivity.getAttendanceItemList().add(new AttendanceItem(tempStrIn[0], tempStrOut[0], makeTimeDiff(inOutTimeDiff), tempStrIn[1], tempStrOut[1], "CHECKINOUT", inTimeInMillis, outTimeInMillis));

                showAlertDialog("Attendance Marked!");
            }
        } else if (state.equalsIgnoreCase("absent")) {
            mainActivity.getAttendanceItemList().add(new AttendanceItem(getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "ABSENT", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
            showAlertDialog("Absent marked!");

        } else if (state.equalsIgnoreCase("holiday")) {
            mainActivity.getAttendanceItemList().add(new AttendanceItem(getFinalDate(), getFinalDate(), "09:00", "09:00:00 AM", "06:00:00 PM", "HOLIDAY", (Calendar.getInstance().getTime()).getTime(), (Calendar.getInstance().getTime()).getTime()));
            showAlertDialog("Holiday marked!");
        }
        mainActivity.addToAttachmentItemList(attendanceItem);
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


    public void showAlertDialog(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
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

    private String findAmorPm(int hour, int minute) {
        String timeSet = "";
        if (hour > 12) {
            timeSet = "PM";
        } else if (hour == 0) {
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }
        return timeSet;
    }


    public String makeTime(int hour, int minutes) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        return new StringBuilder().append(hour).append(':')
                .append(min).append(":00").append(" ").append(timeSet).toString();
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
            str = strArray[1] + " " + strArray[2] + " " + strArray[5] + ", " + amPMTime;
            Log.e("Harsh", date.toString() + " " + date.getTime() + "final String " + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    private String makeTimeDiff(long timeInMillis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)));
    }

}
