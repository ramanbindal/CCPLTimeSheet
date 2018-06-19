package com.example.cub11.ccpltimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cub11.ccpltimesheet.database.DbOpenHelper;

public class MainActivity extends AppCompatActivity {
    private DbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DbOpenHelper(this);
    }
}
