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

    private EditText etUserName, etPassword, etEmail;
    private Button btnSignUp;
    private CheckBox cbNotification;
    private TextView tvLogin;
    private User oldUser;
    private UsersViewModel viewModel;

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

    @Override
    protected void initializeViews() {
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPasswordB);
        //cbNotification = findViewById(R.id.cbNotification);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
    }

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

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    private void saveUserToPreferences(User user) {
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.apply();
    }


    @Override
    public void setValidation() {
        Validator.add(new TextRule(etUserName, RuleOperation.REQUIRED, "Username is required"));
        Validator.add(new NameRule(etUserName, RuleOperation.NAME, "Username is not valid"));
        Validator.add(new EmailRule(etEmail, RuleOperation.REQUIRED, "Email is required"));
        Validator.add(new EmailRule(etEmail, RuleOperation.TEXT, "Email is not valid"));
        Validator.add(new PasswordRule(etPassword, RuleOperation.REQUIRED, "Password is required"));
        // Uncomment below if you have custom PASSWORD rule implemented
        // Validator.add(new PasswordRule(etPassword, RuleOperation.PASSWORD, "Password is not valid"));
    }

    @Override
    public boolean validate() {
        return Validator.validate();
    }
}
