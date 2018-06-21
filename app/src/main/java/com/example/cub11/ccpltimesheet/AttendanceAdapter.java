package com.example.cub11.ccpltimesheet;

/**
 * Created by cub05 on 6/20/2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;

import org.w3c.dom.Text;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    private List<AttendanceItem> attendanceItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, totalTime, inTime, outTime;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.attendance_list_item_date);
            totalTime = view.findViewById(R.id.attendance_list_item_total_time);
            inTime = view.findViewById(R.id.attendance_list_item_in_time);
            outTime = view.findViewById(R.id.attendance_list_item_out_time);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public AttendanceAdapter(List<AttendanceItem> attendanceItemList) {
        this.attendanceItemList = attendanceItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AttendanceItem item = attendanceItemList.get(position);
        holder.date.setText(item.getInDate());

        if (!item.getTotalTime().equals("")) {
            holder.totalTime.setText("( " + item.getTotalTime() + " Hrs)");
        }

        holder.inTime.setText(item.getInTime());
        holder.outTime.setText(item.getOutTime());

        if (item.getType().equals("HOLIDAY")) {
            holder.inTime.setTextColor(Color.BLUE);
            holder.outTime.setTextColor(Color.BLUE);
        } else if (item.getType().equals("ABSENT")) {
            holder.inTime.setTextColor(Color.RED);
            holder.outTime.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return attendanceItemList.size();
    }

    public void removeItem(int position) {
        attendanceItemList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(AttendanceItem item, int position) {
        attendanceItemList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}