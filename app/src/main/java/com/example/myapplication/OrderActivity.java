package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityOrderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {
    int token_count=0,sum=0;
    int meal_count=0;
    private ActivityOrderBinding binding;
    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Database db = new Database(getApplicationContext(), "Dinning_Management", null, 1);

        SharedPreferences sp = getSharedPreferences("shared_prefs",MODE_PRIVATE);
        String Email = sp.getString("Email","Not Found");
        String studentId = db.ret_si(Email);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Orders");

        getAvilableToken(studentId);

        binding.mealPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal_count++;

                binding.showMealBuy.setText(String.valueOf(meal_count));

            }
        });

        binding.mealMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal_count--;
                if(meal_count<0){
                    meal_count=0;
                    Toast.makeText(getApplicationContext(),"wrong attempt",Toast.LENGTH_SHORT).show();
                }
                binding.showMealBuy.setText(String.valueOf(meal_count));

            }
        });

        binding.tokenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token_count++;
                sum=token_count*60;
                binding.showTokenBuy.setText(String.valueOf(token_count));
                binding.tokenCostView.setText(String.valueOf(sum));
            }
        });

        binding.tokenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token_count--;
                sum=token_count*60;
                if(token_count < 0){
                    token_count=0; sum=0;
                    Toast.makeText(getApplicationContext(),"wrong attempt",Toast.LENGTH_SHORT).show();
                }
                binding.showTokenBuy.setText(String.valueOf(token_count));
                binding.tokenCostView.setText(String.valueOf(sum));

            }
        });

        binding.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String token = binding.showTokenBuy.getText().toString();

                //RealTIme Store
                Student student = new Student(studentId,Email,token);

                reference.child(studentId).setValue(student);

                getAvilableToken(studentId);

            }
        });

    }

    private void getAvilableToken(String studentId){
        reference.child(studentId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    Object tokenObj = dataSnapshot.child("tokens").getValue();
                    String tokens = tokenObj != null ? tokenObj.toString() : "0";

                    binding.availableTokenView.setText(tokens);

                }
                else {
                    binding.availableTokenView.setText("0");
                }
            }
        });

    }


}