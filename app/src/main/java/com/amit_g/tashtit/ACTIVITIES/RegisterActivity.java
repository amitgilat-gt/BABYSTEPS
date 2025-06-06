package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.helper.inputValidators.EmailRule;
import com.amit_g.helper.inputValidators.EntryValidation;
import com.amit_g.helper.inputValidators.NameRule;
import com.amit_g.helper.inputValidators.PasswordRule;
import com.amit_g.helper.inputValidators.RuleOperation;
import com.amit_g.helper.inputValidators.TextRule;
import com.amit_g.helper.inputValidators.Validator;
import com.amit_g.model.User;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.UsersViewModel;

public class RegisterActivity extends BaseActivity implements EntryValidation {

    // UI components and view model
    private EditText etUserName, etPassword, etEmail;
    private Button btnSignUp;
    private CheckBox cbNotification;
    private TextView tvLogin;
    private User oldUser;
    private UsersViewModel viewModel;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user2);

        initializeViews();
        setViewModel();
        setValidation();
        setListeners();
    }

    // Initializes views from layout
    @Override
    protected void initializeViews() {
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPasswordB);
        //cbNotification = findViewById(R.id.cbNotification); // optional checkbox for notifications
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
    }

    // Sets up button click listeners
    @Override
    protected void setListeners() {
        btnSignUp.setOnClickListener(v -> {
            if (validate()) {
                User user = new User();
                user.setUserName(etUserName.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());
                //user.setNotifications(cbNotification.isChecked());

                if (oldUser != null) {
                    user.setIdFs(oldUser.getIdFs());
                }

                viewModel.save(user);
                saveUserToPreferences(user);
                navigateToActivity(LoginActivity.class);
            }
        });

        tvLogin.setOnClickListener(v -> navigateToActivity(LoginActivity.class));
    }

    // Initializes the ViewModel
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    // Saves user info to SharedPreferences for later use
    private void saveUserToPreferences(User user) {
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.apply();
    }

    // Sets validation rules for user input fields
    @Override
    public void setValidation() {
        Validator.clear();

        Validator.add(new TextRule(etUserName, RuleOperation.REQUIRED, "Username is required"));
        Validator.add(new NameRule(etUserName, RuleOperation.NAME, "Username is not valid"));
        Validator.add(new EmailRule(etEmail, RuleOperation.REQUIRED, "Email is required"));
        Validator.add(new EmailRule(etEmail, RuleOperation.TEXT, "Email is not valid"));
        Validator.add(new PasswordRule(etPassword, RuleOperation.REQUIRED, "Password is required"));
        Validator.add(new PasswordRule(etPassword, RuleOperation.TEXT,
                "Password must be 4–12 characters and include upper/lowercase letters, a digit, and a symbol"));

    }

    // Validates all input fields
    @Override
    public boolean validate() {
        return Validator.validate();
    }
}
