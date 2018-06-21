package com.example.cub11.ccpltimesheet.view;

import android.app.Dialog;
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

import com.example.cub11.ccpltimesheet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment {

    private static final int COLOR_BLUE = Color.parseColor("#86BFFF");
    DatePicker datePicker;
    TimePicker timePicker;
    EditText timeInEdit;
    EditText timeOutEdit;

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
        working.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                working.setBackgroundColor(COLOR_BLUE);
                holiday.setBackgroundColor(COLOR_WHITE);
                absent.setBackgroundColor(COLOR_WHITE);
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                absent.setBackgroundColor(COLOR_BLUE);
                working.setBackgroundColor(COLOR_WHITE);
                holiday.setBackgroundColor(COLOR_WHITE);
            }
        });
        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return view;
    }

    public void showDialog(final String time) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.time_picker_dialog);
        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);

        final Button submit = (Button) dialog.findViewById(R.id.submitButton);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();


                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");


                String timeIn = "";
                String timeOut = "";

                if (time.equals("TimeIn")) {
                    timeIn = day + "/" + month + "/" + year + " " + hour + ":" + minute;
                    try {
                        date = simpleDateFormat.parse(timeIn);
                        Log.d("Harsh", date.toString() + " " + date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timeInEdit.setText(timeIn);
                    dialog.dismiss();

                } else {

                    timeOut = day + "/" + getMonth(month) + "/" + year + " " + " " + hour + ":" + minute;

                    timeOutEdit.setText(timeOut);
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    public String getMonth(int n) {
        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        return String.valueOf(month[n]);
    }

}
