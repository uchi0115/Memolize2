package com.example.memolize2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String hierarchy;
    private Button register;
    private ListView lvwords;
    ArrayAdapter<String> adapter;
    List<String> wordsList = new ArrayList<>();
    List<String> hierarchies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hierarchies.add("");

        register = findViewById(R.id.bt_register);
        lvwords = findViewById(R.id.lv_words);
        lvwords.setOnItemClickListener(new MainActivity.ListItemClickListener());

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "SELECT * FROM wordlistdb";
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("COL_BEFORE_WORD")).equals("")) {
                    wordsList.add(cursor.getString(cursor.getColumnIndex("COL_WORD")));
                }
            }
        } finally {
            db.close();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList);
        lvwords.setAdapter(adapter);

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RegisterActivity.class);
                intent.putExtra("hierarchy", hierarchy);
                startActivity(intent);
            }
        });

        hierarchy = "";

        ActionBar actionbar = getActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_back_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (hierarchies.get(hierarchies.size() - 1) != "") {
                    hierarchies.remove(hierarchies.size() - 1);
                    hierarchy = hierarchies.get(hierarchies.size() - 1);

                    wordsList.clear();

                    DatabaseHelper helper = new DatabaseHelper(this);
                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        String sql = "SELECT * FROM wordlistdb";
                        Cursor cursor = db.rawQuery(sql, null);

                        while (cursor.moveToNext()) {
                            if (cursor.getString(cursor.getColumnIndex("COL_BEFORE_WORD")).equals("")) {
                                wordsList.add(cursor.getString(cursor.getColumnIndex("COL_WORD")));
                            }
                        }
                    } finally {
                        db.close();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String word = (String) parent.getItemAtPosition(position);

            hierarchy = word;
            hierarchies.add(hierarchy);

            wordsList.clear();

            DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                String sql = "SELECT * FROM wordlistdb";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();

                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex("COL_BEFORE_WORD")).equals(hierarchy)) {
                        wordsList.add(cursor.getString(cursor.getColumnIndex("COL_WORD")));
                    }
                }
                lvwords.setAdapter(adapter);
            } finally {
                db.close();
            }
        }
    }
}