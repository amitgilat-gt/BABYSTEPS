package com.amit_g.tashtit.ACTIVITIES;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_baby_sign);  // Ensure layout is set first

        initializeViews();  // Then initialize views
        setSpinner();       // Then set the spinner adapter
        setViewModel();     // Finally, set the ViewModel
        setValidation();;   // Ensure validation rules are set here
    }

    private void setSpinner() {
        // Make sure spGender is initialized before using it
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
        spGender.setAdapter(adapter); // Now spGender is properly initialized
    }

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
        // Ensure spGender is initialized here
        spGender = findViewById(R.id.spGender);
        setListeners();  // After all views are initialized, set listeners
    }

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
                    // Check for gender selection
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
                        new Handler().postDelayed(() -> {hideProgressDialog();navigateToActivity(HomeActivity.class);}, 1500);
                    }, 1500);

                }
            }
        });
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);

    }


    @Override
    public void setValidation() {
        Validator.clear();

        // Baby name validations
        Validator.add(new TextRule(etBabyName, RuleOperation.REQUIRED, "Baby's name is required"));
        Validator.add(new NameRule(etBabyName, RuleOperation.NAME, "Baby's name is not valid"));

        // Birthdate validations
        Validator.add(new TextRule(etBirthDate, RuleOperation.REQUIRED, "Birthdate is required"));
    }




    @Override
    public boolean validate() {
        return Validator.validate();
    }
}
