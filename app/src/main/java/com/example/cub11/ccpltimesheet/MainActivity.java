package com.example.cub11.ccpltimesheet;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.cub11.ccpltimesheet.database.DbOpenHelper;
import com.example.cub11.ccpltimesheet.database.model.AttendanceItem;
import com.example.cub11.ccpltimesheet.view.BookmarkFragment;
import com.example.cub11.ccpltimesheet.view.HistoryFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DbOpenHelper db;
    public List<AttendanceItem> attendanceItemList;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_bookmarks: {
                        selectedFragment = BookmarkFragment.newInstance();
                    break;
                    }
                    case R.id.action_history: {
                        selectedFragment = HistoryFragment.newInstance();
                    break;
                    }
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, BookmarkFragment.newInstance());
        transaction.commit();


        db=new DbOpenHelper(this);

    }
}

