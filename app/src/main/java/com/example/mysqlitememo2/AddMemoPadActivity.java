package com.example.mysqlitememo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddMemoPadActivity extends AppCompatActivity {

    Button add_button;
    TextView edit_title, edit_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo_pad);

        add_button = findViewById(R.id.add_button);
        edit_title = findViewById(R.id.edit_title);
        edit_memo = findViewById(R.id.edit_memo);


        NHSDatabaseHelper helper = new NHSDatabaseHelper(this);

        add_button.setOnClickListener(view -> {
            try(SQLiteDatabase database = helper.getWritableDatabase()) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);

                int yy = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                int dd = calendar.get(Calendar.DATE);
                int hh = calendar.get(Calendar.HOUR);
                int mm = calendar.get(Calendar.MINUTE);
                int ss = calendar.get(Calendar.SECOND);
                String nowDate = yy + "/" + MM + "/" + dd + " " + hh + ":" + mm + ":" + ss;

                ContentValues contentValues = new ContentValues();
                contentValues.put("title", edit_title.getText().toString());
                contentValues.put("memo", edit_memo.getText().toString());
                contentValues.put("write_date", nowDate);
                database.insert("memopad", null, contentValues);

                Toast.makeText(this, edit_title.getText().toString()+ "書き込みました", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}