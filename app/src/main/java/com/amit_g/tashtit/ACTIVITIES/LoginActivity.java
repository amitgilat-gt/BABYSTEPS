package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.model.User;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.UsersViewModel;

public class LoginActivity extends BaseActivity {

    // UI components and view model
    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegister;
    private UsersViewModel viewModel;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setListeners();
    }

    // Initializes view elements from layout
    @Override
    protected void initializeViews() {
        btnLogin   = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPasswordB);
    }

    // Sets click listeners for login and register actions
    @Override
    protected void setListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Attempt login via ViewModel
                viewModel.loginUser(username, password)
                        .addOnSuccessListener(querySnapshot -> {
                            if (!querySnapshot.isEmpty()) {
                                User user = querySnapshot.getDocuments().get(0).toObject(User.class);
                                saveUserToPreferences(user);
                                navigateToActivity(HomeActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // ⚠️ Handle error
                        });
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(RegisterActivity.class);
            }
        });
    }

    // Saves logged in user data to shared preferences
    private void saveUserToPreferences(User user) {
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userIdFs", user.getIdFs());
        editor.putString("username", user.getUserName());
        editor.putString("email", user.getEmail());
        editor.remove("selectedBabyIdFs");
        editor.apply();
    }

    // Initializes the ViewModel
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }
}
