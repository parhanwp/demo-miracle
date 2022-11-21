package com.example.miraclemarketplace;

import static com.example.miraclemarketplace.DatabaseHelper._ID;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rudderstack.android.sdk.core.RudderClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.rudderstack.android.sdk.core.*;


public class BuyItemActivity extends AppCompatActivity {

    EditText quantity;
    TextView stock, name, price;
    RadioGroup payment_method;
    RadioButton radioButton;
    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        final RudderClient rudderClient = RudderstackClient.getRudderClient();
        final RudderClient rudderClient = MyApplication.getInstance().getRudderClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        stock = findViewById(R.id.stock);
        quantity = findViewById(R.id.quantity);
        payment_method = findViewById(R.id.payment_method);
        name = findViewById(R.id.item_name);
        price = findViewById(R.id.price);
        Button checkout = findViewById(R.id.buy);
//        Button location = findViewById(R.id.seller_location);

        SharedPreference preference = new SharedPreference(this);

        dbManager = new DatabaseManager(this);
        dbManager.open();

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id_item", 0);

        final Cursor cursor = dbManager.getDetailItem(id);
        stock.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STOCK))));
        name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
        price.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PRICE))));

        final Cursor user = dbManager.getProfile(preference.getString("email"));

        final int stok = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.STOCK));

//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), LocationActivity.class);
//                startActivity(intent);
//            }
//        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(quantity.getText().toString());
                //int balance = user.getInt(user.getColumnIndex(DatabaseHelper.BALANCE));
                int total = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PRICE)) * qty;
                if (quantity.getText().toString().equals("")){
                    Toast.makeText(view.getContext(), "Quantity tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (qty > stok) {
                    Toast.makeText(view.getContext(), "Tidak boleh lebih dari stok", Toast.LENGTH_SHORT).show();
                }
//                else if(balance < total){
//                    Toast.makeText(view.getContext(), "Balance tidak cukup", Toast.LENGTH_SHORT).show();
//                }
                else {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    dbManager.updateStock(id, stok-qty);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(new Date());
                    dbManager.storeTransaction(user.getInt(user.getColumnIndex(_ID)), id, qty, date);
                   // dbManager.updateBalance(user.getInt(user.getColumnIndex(_ID)), balance - total);
                    int getPayment = payment_method.getCheckedRadioButtonId();
                    radioButton = findViewById(getPayment);
                    startActivity(intent);
                    Log.d("log_adam",price.getText().toString());
                    rudderClient.track(
                            "Product_Checkout",
                            new RudderProperty()
                                    .putValue("name",name.getText().toString())
                                    .putValue("prices",total)
                                    .putValue("quantity",quantity.getText().toString())
                                    .putValue("payment_method",radioButton.getText())
                                    .putValue("stock",stock.getText().toString())
                    );
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buy Item Activity");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}