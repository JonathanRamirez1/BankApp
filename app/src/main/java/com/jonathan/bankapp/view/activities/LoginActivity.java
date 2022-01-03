package com.jonathan.bankapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.jonathan.bankapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     //   Objects.requireNonNull(getSupportActionBar()).hide();
        setSupportActionBar(findViewById(R.id.toolbarLogin));
    }
}