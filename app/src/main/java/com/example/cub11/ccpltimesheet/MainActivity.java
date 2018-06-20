package com.example.cub11.ccpltimesheet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ScrollView;

import com.example.cub11.ccpltimesheet.database.DbOpenHelper;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private AttendanceAdapter attendanceAdapter;
    List<AttendanceItem> attendanceItemList;
    private ScrollView scrollView;
    private DbOpenHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbOpenHelper(this);


        scrollView = (ScrollView) findViewById(R.id.activity_main_container);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_rv);

        attendanceItemList = new ArrayList<>();
        attendanceItemList.add(new AttendanceItem(1, "12-25-2014", "09:00", "12:02:12", "08:22:12", "ABSENT"));
        attendanceItemList.add(new AttendanceItem(2, "12-25-2014", "09:00", "12:02:12", "08:22:12", "HOLIDAY"));
        attendanceItemList.add(new AttendanceItem(3, "12-25-2014", "09:00", "12:02:12", "08:22:12", "dffdw"));
        attendanceItemList.add(new AttendanceItem(4, "12-25-2014", "09:00", "12:02:12", "08:22:12", ""));
        attendanceAdapter = new AttendanceAdapter(getApplicationContext(), attendanceItemList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(attendanceAdapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AttendanceAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = attendanceItemList.get(viewHolder.getAdapterPosition()).getDate();

            // backup of removed item for undo purpose
            final AttendanceItem deletedItem = attendanceItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            attendanceAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(scrollView, name + " Removed from List!", Snackbar.LENGTH_LONG);
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
