package com.jonathan.banco.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jonathan.banco.R;
import com.jonathan.banco.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
