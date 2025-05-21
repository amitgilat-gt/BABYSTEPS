package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.model.Baby;
import com.amit_g.model.UserBaby;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.UserBabyViewModel;

public class ConnectToBabyActivity extends BaseActivity {

    // UI elements and view models
    private EditText etIdFs;
    private EditText etPasswordB;
    private Button btnConnect;
    private Button btnCancel;
    private BabiesViewModel babiesViewModel;
    private UserBabyViewModel userBabyViewModel;
    private SharedPreferences sharedPreferences;
    private String userId;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connect_to_baby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setListeners();
    }

    // Initializes view components and retrieves userId from shared preferences
    @Override
    protected void initializeViews() {
        etIdFs = findViewById(R.id.etIdFs);
        etPasswordB = findViewById(R.id.etPasswordB);
        btnConnect = findViewById(R.id.btnConnect);
        btnCancel = findViewById(R.id.btnCancel);
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userIdFs", null);
    }

    // Sets click listeners for connect and cancel buttons
    @Override
    protected void setListeners() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etIdFs.getText().toString();
                String password = etPasswordB.getText().toString();

                // Tries to connect to a baby by ID and password
                babiesViewModel.connectBaby(id, password)
                        .addOnSuccessListener(querySnapshot -> {
                            if (!querySnapshot.isEmpty()) {
                                Baby baby = querySnapshot.getDocuments().get(0).toObject(Baby.class);
                                UserBaby userBaby = new UserBaby();
                                userBaby.setBabyId(baby.getIdFs());
                                userBaby.setUserId(userId);
                                userBabyViewModel.add(userBaby);
                                navigateToActivity(HomeActivity.class);
                            } else {
                                Toast.makeText(ConnectToBabyActivity.this, "Invalid id or password", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // ⚠️ Handle error
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancels the connection and closes the activity
                finish();
            }
        });
    }

    // Initializes the ViewModels used in this activity
    @Override
    protected void setViewModel() {
        babiesViewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);
    }
}
