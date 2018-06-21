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
    Button doneBtn;
    String state = "working";

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
        AttendanceItem attendanceItem=new AttendanceItem();
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
            }
            else
            {

            }
        } else if (state.equalsIgnoreCase("absent")) {

        } else if (state.equalsIgnoreCase("holiday")) {

        }

        mainActivity.addToAttachmentItemList(attendanceItem);

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
                        Log.d("Harsh", date.toString());
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
