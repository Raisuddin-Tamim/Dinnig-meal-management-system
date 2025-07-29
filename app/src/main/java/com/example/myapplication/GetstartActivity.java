package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityGetstartBinding;

public class GetstartActivity extends AppCompatActivity {

    Button getStartedBtn;
    private ActivityGetstartBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getstart);

         binding = ActivityGetstartBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.appName, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("LoggedIn", false);
        if(isLoggedIn){
            startActivity(new Intent(GetstartActivity.this, AppDrawerActivity.class));
        }
        else{
            Toast.makeText(getApplicationContext(), "Please Login First", Toast.LENGTH_SHORT).show();
        }

        binding.getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetstartActivity.this, LoginActivity.class));

            }
        });
    }

}