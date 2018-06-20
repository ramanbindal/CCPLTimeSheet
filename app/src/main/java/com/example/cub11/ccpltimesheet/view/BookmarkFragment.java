package com.example.cub11.ccpltimesheet.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cub11.ccpltimesheet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment implements View.OnClickListener {
    private Button punchInBtn, punchOutBtn, absentBtn, holidayBtn;

    public BookmarkFragment() {

        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        punchInBtn = view.findViewById(R.id.punchIn);
        punchOutBtn = view.findViewById(R.id.punchOut);
        absentBtn = view.findViewById(R.id.absent);
        holidayBtn = view.findViewById(R.id.holiday);
        punchOutBtn.setOnClickListener(this);
        punchInBtn.setOnClickListener(this);
        absentBtn.setOnClickListener(this);
        holidayBtn.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.absent:

                break;
            case R.id.holiday:
                break;
            case R.id.punchIn:
                break;
            case R.id.punchOut:
                break;

        }

    }
}
