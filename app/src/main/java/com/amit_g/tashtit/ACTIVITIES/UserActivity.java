package com.amit_g.tashtit.ACTIVITIES;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.helper.inputValidators.EmailRule;
import com.amit_g.helper.inputValidators.EntryValidation;
import com.amit_g.helper.inputValidators.NameRule;
import com.amit_g.helper.inputValidators.PasswordRule;
import com.amit_g.helper.inputValidators.Rule;
import com.amit_g.helper.inputValidators.RuleOperation;
import com.amit_g.helper.inputValidators.TextRule;
import com.amit_g.helper.inputValidators.Validator;
import com.amit_g.model.User;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.UsersViewModel;

public class UserActivity extends BaseActivity implements EntryValidation {

    private EditText etUserName;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignUp;
    private CheckBox cbNotification;
    private User oldUser;
    private UsersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user2);

        initializeViews();
        setListeners();
        setViewModel();
        setValidation();  // Ensure validation rules are set here
    }

    @Override
    protected void initializeViews() {
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbNotification = findViewById(R.id.cbNotification);
        btnSignUp = findViewById(R.id.btnSignUp);

        setListeners();
    }

    @Override
    protected void setListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    User user = new User();
                    user.setUserName(etUserName.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.setNotifications(cbNotification.isChecked());

                    if (oldUser != null) {
                        user.setIdFs(oldUser.getIdFs());
                    }
                    viewModel.save(user);
                }
            }
        });
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

//    @Override
//    public void setValidation() {
//        Validator.add(new Rule(etUserName, RuleOperation.REQUIRED, "Username is required"));
//        Validator.add(new Rule(etUserName, RuleOperation.NAME, "Username is not valid"));
//        Validator.add(new Rule(etEmail, RuleOperation.REQUIRED, "Email is required"));
//        Validator.add(new Rule(etEmail, RuleOperation.TEXT, "Email is not valid"));
//        Validator.add(new Rule(etPassword, RuleOperation.REQUIRED, "Password is required"));
//        Validator.add(new Rule(etPassword, RuleOperation.PASSWORD, "Password is not valid"));
//
//    }
@Override
public void setValidation() {
    // Assuming TextRule and NameRule are subclasses of Rule
    Validator.add(new TextRule(etUserName, RuleOperation.REQUIRED, "Username is required"));
    Validator.add(new NameRule(etUserName, RuleOperation.NAME, "Username is not valid"));
    Validator.add(new EmailRule(etEmail, RuleOperation.REQUIRED, "Email is required"));
    Validator.add(new EmailRule(etEmail, RuleOperation.TEXT, "Email is not valid"));
    Validator.add(new PasswordRule(etPassword, RuleOperation.REQUIRED, "Password is required"));
    //Validator.add(new PasswordRule(etPassword, RuleOperation.PASSWORD, "Password is not valid"));
}


    @Override
    public boolean validate() {
        return Validator.validate();  // Check all rules and return the result
    }
}
