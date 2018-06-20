package com.example.cub11.ccpltimesheet.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cub11.ccpltimesheet.AttendanceAdapter;
import com.example.cub11.ccpltimesheet.MainActivity;
import com.example.cub11.ccpltimesheet.R;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cub11.ccpltimesheet.RecyclerItemTouchHelper;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    private RecyclerView recyclerView;
    private AttendanceAdapter attendanceAdapter;
    private List<AttendanceItem> attendanceItemList;
    private LinearLayout linearLayout;


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.historyfragment, container, false);


        ImageView button = (ImageView) view.findViewById(R.id.closeButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment2 = new DatePickerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        attendanceItemList = activity.getAttendanceItemList();

        setUpRecyclerView(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void setUpRecyclerView(View view) {
        linearLayout = view.findViewById(R.id.history_fragment_container);
        recyclerView = view.findViewById(R.id.history_fragment_rv);
        attendanceAdapter = new AttendanceAdapter(attendanceItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(attendanceAdapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AttendanceAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = attendanceItemList.get(viewHolder.getAdapterPosition()).getInDate();

            // backup of removed item for undo purpose
            final AttendanceItem deletedItem = attendanceItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            attendanceAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(linearLayout, name + " Removed from List!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    attendanceAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}
