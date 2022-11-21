package com.example.miraclemarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.rudderstack.android.sdk.core.*;

public class RegisterActivity extends AppCompatActivity {
    EditText fullName, email, password, confirm, phone;
    RadioGroup group;
    CheckBox agree;
    Button register;

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        phone = findViewById(R.id.phone);
        group = findViewById(R.id.radio_group);
        agree = findViewById(R.id.checkbox);
        register = findViewById(R.id.register);
        dbManager = new DatabaseManager(this);
        dbManager.open();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClicked();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register Activity");
    }

    private void onRegisterButtonClicked(){
        final RudderClient rudderClient = RudderstackClient.getRudderClient();
        String number = phone.getText().toString().replace("+", "");
        RadioButton button = findViewById(group.getCheckedRadioButtonId());
        if (fullName.getText().toString().equals("")) {
            Toast.makeText(this, "Full Name tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (email.getText().toString().equals("")) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (confirm.getText().toString().equals("")) {
            Toast.makeText(this, "Confirm Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }  else if (!confirm.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(this, "Confirm Password tidak sesuai", Toast.LENGTH_SHORT).show();
        } else if (phone.getText().toString().equals("")) {
            Toast.makeText(this, "Phone", Toast.LENGTH_SHORT).show();
        } else if (fullName.getText().length() < 2) {
            Toast.makeText(this, "Full Name harus lebih dari 2 huruf", Toast.LENGTH_SHORT).show();
        } else if (email.getText().length() < 5 && email.getText().length() > 25) {
            Toast.makeText(this, "Email harus lebih dari 5 dan kurang dari 25 huruf", Toast.LENGTH_SHORT).show();
        } else if (!cekHurufBesar(password.getText().toString())){
            Toast.makeText(this, "Password harus mengandung huruf kapital, 1 simbol, 1 angka", Toast.LENGTH_SHORT).show();
        } else if (!cekAngka(password.getText().toString())) {
            Toast.makeText(this, "Password harus mengandung angka", Toast.LENGTH_SHORT).show();
        } else if (!cekSpesial(password.getText().toString())) {
            Toast.makeText(this, "Password harus mengandung simbol", Toast.LENGTH_SHORT).show();
        } else if (password.getText().length() > 15) {
            Toast.makeText(this, "Password tidak boleh lebih dari 15", Toast.LENGTH_SHORT).show();
        } else if (!phone.getText().toString().contains("+62")) {
            Toast.makeText(this, "Nomer Telepon harus ada +62", Toast.LENGTH_SHORT).show();
        } else if (phone.getText().length() > 15) {
            Toast.makeText(this, "Nomer Telepon tidak boleh lebih dari 15", Toast.LENGTH_SHORT).show();
        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(phone.getText().toString())) {
            Toast.makeText(this, "Nomer Telepon harus angka semua", Toast.LENGTH_SHORT).show();
        } else if (button.getText() == null) {
            Toast.makeText(this, "Tolong pilih gender anda", Toast.LENGTH_SHORT).show();
        } else if (!agree.isChecked()) {
            Toast.makeText(this, "Tolong setujui pernyataan", Toast.LENGTH_SHORT).show();
        } else if(dbManager.checkUsername(email.getText().toString())) {
            Toast.makeText(this, "Username telah digunakan", Toast.LENGTH_SHORT).show();
        } else {
            dbManager.register(fullName.getText().toString(), email.getText().toString(), password.getText().toString(), phone.getText().toString(), button.getText().toString());
            Intent intent = new Intent(this, LoginActivity.class);
            RudderTraits traits = new RudderTraits();
            traits.putEmail(email.getText().toString());
            traits.putPhone(phone.getText().toString());
            rudderClient.identify(email.getText().toString(),traits,null);
            rudderClient.track(
                    "register",
                    new RudderProperty()
                            .putValue("email",email.getText().toString())
            );
            startActivity(intent);
            finish();
        }
    }

    private boolean cekHurufBesar(String password){
        for (int i = 0 ; i < password.length() ; i++){
            Character hurufBesar = password.charAt(i);

            if(Character.isUpperCase(hurufBesar)){
                return true;
            }

        }
        return false;
    }

    private boolean cekAngka(String password){
        for (int i = 0 ; i < password.length() ; i++){
            Character angka = password.charAt(i);

            if(Character.isDigit(angka)){
                return true;
            }

        }
        return false;
    }

    private boolean cekSpesial(String password){
        String spesialChar = "~`!@#$%^&*()-_=+|[{]};:',<.>/?";

        for (int i = 0 ; i < password.length() ; i++){
            Character spesial = password.charAt(i);
            if(spesialChar.contains(String.valueOf(spesial))){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}