package com.example.dotamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseManager dbManager;
    final String[] from = new String[]{DatabaseHelper.QUANTITY, DatabaseHelper.TRANSACTION_DATE};
    final int[] to = new int[]{R.id.qty, R.id.date};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.history);

        dbManager = new DatabaseManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetchTransaction();

        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.history_list, cursor, from, to);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        Button button = findViewById(R.id.clear);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.deleteHistory();
                listView.setVisibility(View.INVISIBLE);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History Activity");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}