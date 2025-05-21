package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.helper.DateUtil;
import com.amit_g.helper.inputValidators.DateRule;
import com.amit_g.helper.inputValidators.EntryValidation;
import com.amit_g.helper.inputValidators.Rule;
import com.amit_g.helper.inputValidators.RuleOperation;
import com.amit_g.helper.inputValidators.TextRule;
import com.amit_g.helper.inputValidators.Validator;
import com.amit_g.model.Progress;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ProgressViewModel;

import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GrowthActivity extends BaseActivity implements EntryValidation {

    // UI components and view model
    private EditText etHeight;
    private EditText etWeight;
    private EditText etDate;
    private Button btnPut;
    private Button btnCancelSt;
    private ProgressViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private Progress progress;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_growth);
        initializeViews();
        setValidation();
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        setListeners();
        setViewModel();
    }

    // Initializes all views and handles pre-filled data if editing an entry
    @Override
    protected void initializeViews() {
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etDate = findViewById(R.id.etDate);
        btnPut = findViewById(R.id.btnPut);
        btnCancelSt = findViewById(R.id.btnCancelSt);
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Set today's date by default
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            etDate.setText(LocalDate.now().format(formatter));
        }

        // Load existing progress if passed for editing
        Progress passedProgress = (Progress) getIntent().getSerializableExtra("progress");
        if (passedProgress != null) {
            btnPut.setText("Update");
            etHeight.setText(String.valueOf(passedProgress.getHeight()));
            etWeight.setText(String.valueOf(passedProgress.getWeight()));
            etDate.setText(DateUtil.longDateToString(passedProgress.getDate()));
            progress = passedProgress;
        } else {
            progress = new Progress();
        }
    }

    // Sets listeners for save and cancel buttons
    @Override
    protected void setListeners() {
        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progress.setHeight(Double.parseDouble(etHeight.getText().toString()));
                    progress.setWeight(Double.parseDouble(etWeight.getText().toString()));
                    progress.setDate(DateUtil.stringDateToLong(etDate.getText().toString()));
                    String babyId = sharedPreferences.getString("selectedBabyIdFs", null);
                    progress.setBabyId(babyId);

                    if(btnPut.getText().equals("Update"))
                        viewModel.update(progress);
                    else
                        viewModel.add(progress);

                    Toast.makeText(GrowthActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnCancelSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity without saving
                finish();
            }
        });
    }

    // Initializes the view model for handling growth entries
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
    }

    // Sets validation rules for height, weight, and date
    @Override
    public void setValidation() {
        Validator.add(new TextRule(etHeight, RuleOperation.REQUIRED, "Height is required"));
        Validator.add(new TextRule(etWeight, RuleOperation.REQUIRED, "Weight is required"));
        Validator.add(new TextRule(etDate, RuleOperation.REQUIRED, "Date is required"));
    }

    // Triggers the validation check for all fields
    @Override
    public boolean validate() {
        return Validator.validate();
    }
}

