package com.example.memolize2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {

    String saveWord, saveBeforeWord, hierarchy;
    private Button save, change;
    private EditText input1, input2;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        hierarchy = intent.getStringExtra("hierarchy");

        input1 = findViewById(R.id.input1);
        save = findViewById((R.id.b_save));
        change = findViewById(R.id.b_change);
        output = findViewById(R.id.output);

        save.setOnClickListener(RegisterActivity.this);
        change.setOnClickListener(RegisterActivity.this);
    }

    public void onClick(View view) {
        if (view == save) {
            DatabaseHelper helper = new DatabaseHelper(RegisterActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            saveWord = input1.getText().toString();

            try {
                String sql_acc_Insert = "INSERT INTO wordlistdb (COL_WORD, COL_BEFORE_WORD) VALUES (?, ?)";
                SQLiteStatement stmt = db.compileStatement(sql_acc_Insert);

                stmt.bindString(1, saveWord);
                stmt.bindString(2, hierarchy);
                stmt.executeInsert();

            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, "データの保存に失敗しました", Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }

            output.setText(saveWord);

        } else if (view == change) {
            finish();
        }
    }
}