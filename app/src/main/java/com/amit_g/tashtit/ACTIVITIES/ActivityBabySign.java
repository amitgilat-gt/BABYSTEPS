package com.amit_g.tashtit.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.helper.DateUtil;
import com.amit_g.helper.inputValidators.DateRule;
import com.amit_g.helper.inputValidators.EntryValidation;
import com.amit_g.helper.inputValidators.NameRule;
import com.amit_g.helper.inputValidators.Rule;
import com.amit_g.helper.inputValidators.RuleOperation;
import com.amit_g.helper.inputValidators.TextRule;
import com.amit_g.helper.inputValidators.Validator;
import com.amit_g.model.Baby;
import com.amit_g.model.Gender;
import com.amit_g.model.UserBaby;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.UserBabyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityBabySign extends BaseActivity implements EntryValidation {

    // UI components and view models
    private EditText etBabyName;
    private EditText etBirthDate;
    private FloatingActionButton fabPicture;
    private Button btnSignBaby;
    private BabiesViewModel viewModel;
    private Baby baby;
    private EditText etBabyPassword;
    private Spinner spGender;
    private UserBabyViewModel userBabyViewModel;
    private SharedPreferences sharedPreferences;
    private String userId;
    private EditText etId;
    private Button btnCancel;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_baby_sign);
        initializeViews();
        setSpinner();
        setViewModel();
        setValidation();
    }

    // Initializes gender selection spinner
    private void setSpinner() {
        if (spGender == null) {
            spGender = findViewById(R.id.spGender);
        }

        List<String> genderList = new ArrayList<>();
        genderList.add("Select Gender");
        for (Gender gender : Gender.values()) {
            genderList.add(gender.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

    // Initializes view components and retrieves userId from shared preferences
    @Override
    protected void initializeViews() {
        etBabyName = findViewById(R.id.etBabyName);
        etBirthDate = findViewById(R.id.etBirthDate);
        fabPicture = findViewById(R.id.fabAddPhoto);
        btnSignBaby = findViewById(R.id.btnSignUp);
        etBabyPassword = findViewById(R.id.etBabyPassword);
        etId = findViewById(R.id.etId);
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userIdFs", null);
        spGender = findViewById(R.id.spGender);
        btnCancel = findViewById(R.id.btnCancel);
        setListeners();
    }

    // Sets click listener on the sign-up button
    @Override
    protected void setListeners() {
        btnSignBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    baby = new Baby();
                    baby.setName(etBabyName.getText().toString());
                    baby.setBirthDate(DateUtil.stringDateToLong(etBirthDate.getText().toString()));
                    baby.setPassword(etBabyPassword.getText().toString());

                    String selectedGender = spGender.getSelectedItem().toString();
                    if (selectedGender.equals("Select Gender")) {
                        Toast.makeText(ActivityBabySign.this, "Please select gender", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    baby.setId(etId.getText().toString());
                    Gender gender = Gender.valueOf(selectedGender.toUpperCase());
                    baby.setGender(gender);
                    viewModel.save(baby);

                    showProgressDialog("Signing Baby", "Please wait...");
                    new Handler().postDelayed(() -> {
                        UserBaby userBaby = new UserBaby();
                        userBaby.setBabyId(baby.getIdFs());
                        userBaby.setUserId(userId);
                        userBabyViewModel.add(userBaby);
                        new Handler().postDelayed(() -> {
                            hideProgressDialog();
                            navigateToActivity(HomeActivity.class);
                        }, 1500);
                    }, 1500);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Initializes view models
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);
    }

    // Sets validation rules for baby name and birth date
    @Override
    public void setValidation() {
        Validator.clear();

        // Validate baby name
        Validator.add(new TextRule(etBabyName, RuleOperation.REQUIRED, "Baby's name is required"));
        Validator.add(new NameRule(etBabyName, RuleOperation.NAME, "Baby's name is not valid"));

        // Validate birthdate presence
        Validator.add(new TextRule(etBirthDate, RuleOperation.REQUIRED, "Birthdate is required"));

        // Validate age: must be less than 6 years
        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate today = LocalDate.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate sixYearsAgo = today.minusYears(6);

        Validator.add(new DateRule(etBirthDate, RuleOperation.DATE, "Baby must be younger than 6 years",
                sixYearsAgo, today));

        // Validate baby ID
        Validator.add(new TextRule(etId, RuleOperation.REQUIRED, "ID is required"));
        Validator.add(new TextRule(etId, RuleOperation.TEXT, "ID must be 9 digits", 9, 9, true));
    }



    // Validates all fields based on rules defined
    @Override
    public boolean validate() {
        return Validator.validate();
    }
}

