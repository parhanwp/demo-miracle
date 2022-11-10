package com.example.dotamarketplace;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rudderstack.android.sdk.core.*;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DatabaseManager dbManager;
    final String[] from = new String[]{DatabaseHelper.NAME, DatabaseHelper.PRICE, DatabaseHelper.STOCK};
    final int[] to = new int[]{R.id.item_name, R.id.item_price, R.id.item_stok};
    ProgressBar bar;



    //sms
    private SmsManager smsManager;
    private int sendSmsPermission;
    private int receiveSmsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.bar);
        dbManager = new DatabaseManager(this);
        dbManager.open();
        if (dbManager.checkItemTable()) {
            getData();
        } else {

            bar.setVisibility(View.GONE);
        }
        listView = findViewById(R.id.list_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Activity");
        Cursor cursor = dbManager.fetchItem();
        ItemAdapter itemAdapter = new ItemAdapter(this, R.layout.item_list, cursor, from, to);
        itemAdapter.notifyDataSetChanged();
        listView.setAdapter(itemAdapter);
        SharedPreference preference = new SharedPreference(this);
//        db.setForeignKeyConstraintsEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent1 = new Intent(this, HistoryActivity.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://raw.githubusercontent.com/almrdcprio/database-try/main/products.json";
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = null;
                            try {
                                item = response.getJSONObject(i);
                                String name = item.getString("name");
                                int price = item.getInt("price");
                                int stock = item.getInt("stock");
                                dbManager.storeItem(name, price, stock);
                                Cursor cursor = dbManager.fetchItem();
                                ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext(), R.layout.item_list, cursor, from, to);
                                itemAdapter.notifyDataSetChanged();
                                bar.setVisibility(View.GONE);
                                listView.setAdapter(itemAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(request);
    }

    public void setSmsPermission() {
        smsManager = SmsManager.getDefault();

        sendSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        receiveSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (receiveSmsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
