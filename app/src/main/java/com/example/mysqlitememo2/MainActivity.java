package com.example.mysqlitememo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView sql_title;
    int id = 0;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        deleteDatabase("nhs10627db");

        sql_title = findViewById(R.id.title);
        FloatingActionButton add_button = findViewById(R.id.add_button);


        ArrayList<HashMap<String, String >> data = new ArrayList<>();
        NHSDatabaseHelper helper = new NHSDatabaseHelper(this);
        String[] cols = {"id", "title", "memo", "write_date"};
        try (SQLiteDatabase database = helper.getWritableDatabase()) {
            int cnt = 0;
            cursor = database.query("memopad", cols, null, null, null, null, null, null);

            boolean eol = cursor.moveToFirst();
            if (eol) {
                while (eol){
                    cnt++;
                    HashMap<String, String > item = new HashMap<>();
                    item.put("id", cursor.getString(0));
                    item.put("title", cursor.getString(1));
                    item.put("memo", cursor.getString(2));
                    item.put("write_date", cursor.getString(3));
                    data.add(item);
                    eol = cursor.moveToNext();
                }
                Log.d("TEST" , String.valueOf(cnt));
                sql_title.setText("メモ一覧（" + cnt + "件）");
            } else {
                Toast.makeText(this, "指定されたタイトルはありません", Toast.LENGTH_LONG).show();
            }
        }


        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item,
                new String[] {"title", "write_date"},
                new int[] {R.id.title, R.id.date});
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, MemoPadActivity.class);
            Log.d("TEST", data.get(i).get("id"));
            intent.putExtra("id", data.get(i).get("id"));
            intent.putExtra("title", data.get(i).get("title"));
            intent.putExtra("memo", data.get(i).get("memo"));
            startActivity(intent);
        }));

        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMemoPadActivity.class);
            startActivity(intent);
        });
    }
}