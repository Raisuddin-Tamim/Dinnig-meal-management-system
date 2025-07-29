package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    int token_count=0,sum=0,meal_plus_minus,to_store_token;
    int meal_count=0;
    private ActivityOrderBinding binding;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    Button dialogNo,dialogYes;



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

        SharedPreferences sp = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String Email = sp.getString("Email", "Not Found");
        String studentId = db.ret_si(Email);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Orders");

        getAvilableToken(studentId);

        binding.mealPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meal_plus_minus > 0) {
                    meal_count++;
                    meal_plus_minus--;
                }
                binding.showMealBuy.setText(String.valueOf(meal_count));
                binding.availableTokenView.setText(String.valueOf(meal_plus_minus));

            }
        });

        binding.mealMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal_count--;
                if (meal_count < 0) {
                    meal_count = 0;
                    Toast.makeText(getApplicationContext(), "wrong attempt", Toast.LENGTH_SHORT).show();
                } else {
                    meal_plus_minus++;
                }
                binding.showMealBuy.setText(String.valueOf(meal_count));
                binding.availableTokenView.setText(String.valueOf(meal_plus_minus));
            }
        });

        binding.tokenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token_count++;
                sum = token_count * 60;
                binding.showTokenBuy.setText(String.valueOf(token_count));
                binding.tokenCostView.setText(String.valueOf(sum));
            }
        });

        binding.tokenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token_count--;
                sum = token_count * 60;
                if (token_count < 0) {
                    token_count = 0;
                    sum = 0;
                    Toast.makeText(getApplicationContext(), "wrong attempt", Toast.LENGTH_SHORT).show();
                }
                binding.showTokenBuy.setText(String.valueOf(token_count));
                binding.tokenCostView.setText(String.valueOf(sum));

            }
        });

        binding.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = binding.showTokenBuy.getText().toString();
                to_store_token += Integer.parseInt(token);
                storeOrderDAta(studentId,Email,to_store_token);
            }
        });

        binding.orderMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogBox(studentId,Email,meal_plus_minus);
            }
        });
    }

    private void storeOrderDAta(String studentId, String Email,int token) {


        //RealTIme Store
        Student student = new Student(studentId, Email, String.valueOf(token));

        reference.child(studentId).setValue(student);

        getAvilableToken(studentId);

    }


    //Read Data from Firebase (Real-Time Database)
    private void getAvilableToken(String studentId){
        reference.child(studentId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    Object tokenObj = dataSnapshot.child("tokens").getValue();
                    String tokens = tokenObj != null ? tokenObj.toString() : "0";
                    to_store_token = meal_plus_minus = Integer.parseInt(tokens);
                    meal_count = 0;
                    binding.showMealBuy.setText(String.valueOf(meal_count));
                    binding.availableTokenView.setText(tokens);

                }
                else {
                    binding.availableTokenView.setText("0");
                }
            }
        });


    }

    private void showDialogBox(String studentId,String Email ,int meal_plus_minus)
    {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_confirmation, null);

        dialogNo = view.findViewById(R.id.dialogNo);
        dialogYes = view.findViewById(R.id.dialogYes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeOrderDAta(studentId,Email,meal_plus_minus);
                alertDialog.dismiss();
            }
        });
        dialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}