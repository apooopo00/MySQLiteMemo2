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

public class MemoPadActivity extends AppCompatActivity {

    Button clear_button, list_button, delete_button, update_button;
    TextView edit_title, edit_memo;

    String id, title, memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_pad);

        clear_button = findViewById(R.id.clear_button);
        list_button= findViewById(R.id.list_button);
        delete_button = findViewById(R.id.delete_button);
        update_button = findViewById(R.id.update_button);
        edit_title = findViewById(R.id.edit_title);
        edit_memo = findViewById(R.id.edit_memo);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        memo = getIntent().getStringExtra("memo");

        edit_title.setText(title);
        edit_memo.setText(memo);



        NHSDatabaseHelper helper = new NHSDatabaseHelper(this);

        clear_button.setOnClickListener(view -> {
            edit_memo.setText("");
        });

        list_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        delete_button.setOnClickListener(view -> {
            try (SQLiteDatabase database = helper.getWritableDatabase()) {
                String[] params = {id};

                if (!id.equals("")){
                    int i = database.delete("memopad", "id = ?", params);
                    if (i == 1){
                        Toast.makeText(this, "削除", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "指定されたタイトルはありません", Toast.LENGTH_LONG).show();
                    }
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        });

        update_button.setOnClickListener(v -> {
            if (!edit_title.getText().toString().equals("")){
                try (SQLiteDatabase database = helper.getWritableDatabase()){
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

                    ContentValues cv = new ContentValues();
                    cv.put("title", edit_title.getText().toString());
                    cv.put("memo", edit_memo.getText().toString());
                    cv.put("write_date", nowDate);
                    String[] params = {id};
                    if (!id.equals("")){
                        database.update("memopad", cv, "id = ?", params);
                        Toast.makeText(this, "アップデート", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            } else {
                Toast.makeText(this, "タイトルが未入力です", Toast.LENGTH_SHORT).show();
            }

        });

    }
}