package com.example.dotamarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rudderstack.android.sdk.core.*;


public class LoginActivity extends AppCompatActivity {

    Button login, register;
    EditText email, password;
    private DatabaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RudderClient rudderClient = MyApplication.getInstance().getRudderClient();
        final String fcm_id = MyApplication.getInstance().getFCMtoken();

        setContentView(R.layout.activity_login);
        dbManager = new DatabaseManager(this);
        dbManager.open();
        Toolbar toolbar = findViewById(R.id.toolbar);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        final SharedPreference preference = new SharedPreference(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = dbManager.login(email.getText().toString());
                    if (cursor.getCount() == 0) {
                        Toast.makeText(LoginActivity.this, "Email belum terdaftar", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.getText().toString().equals(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD)))) {
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            RudderTraits traits = new RudderTraits();
                            traits.putEmail(email.getText().toString());
                            traits.put("firebaseToken",fcm_id);
                            rudderClient.identify(email.getText().toString(),traits,null);
                            preference.saveString("email", email.getText().toString());
                            rudderClient.track(
                                    "login",
                                    new RudderProperty()
                                            .putValue("email",email.getText().toString())
                            );
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login Activity");
    }
}