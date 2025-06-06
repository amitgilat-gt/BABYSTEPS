package com.amit_g.tashtit.ACTIVITIES;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.helper.DateUtil;
import com.amit_g.helper.inputValidators.EntryValidation;
import com.amit_g.helper.inputValidators.Rule;
import com.amit_g.helper.inputValidators.RuleOperation;
import com.amit_g.helper.inputValidators.TextRule;
import com.amit_g.helper.inputValidators.Validator;
import com.amit_g.model.Action;
import com.amit_g.model.LastActivity;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivitiesActivity extends BaseActivity implements EntryValidation {

    // UI components and model
    private EditText etNote;
    private Spinner actionSpinner;
    private Button btnAddNote;
    private Button btnCancelNote;
    private ActivityViewModel viewModel;
    private LastActivity activity;
    private Button btnSelectTime;
    private TextView tvSelectedTime;
    private SharedPreferences sharedPreferences;
    private TextView textView2;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        activity = new LastActivity();
        initializeViews();
        setViewModel();
        setValidation();
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        setListeners();
    }

    // Initializes all view components and handles edit-mode setup if activity is passed
    @Override
    protected void initializeViews() {
        etNote = findViewById(R.id.etNote);
        actionSpinner = findViewById(R.id.spActions);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnCancelNote = findViewById(R.id.btnCancelNote);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        textView2 = findViewById(R.id.textView2);

        // Load passed activity if exists (edit mode)
        LastActivity passedActivity = (LastActivity) getIntent().getSerializableExtra("activity");
        if (passedActivity != null) {
            activity = passedActivity;
            btnAddNote.setText("Update");
            textView2.setText("Update Activity");
            etNote.setText(activity.getDetails());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity.getTime() != 0L) {
                tvSelectedTime.setText(DateUtil.localTimeToString(DateUtil.longToLocalTime(activity.getTime())));
            }

            setSpinner(activity.getAction());
        } else {
            activity = new LastActivity();
            setSpinner(null);
        }
    }

    // Sets click listeners for buttons (select time, save, cancel)
    @Override
    protected void setListeners() {
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ActivitiesActivity.this,
                        (view, selectedHour, selectedMinute) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                // Convert to LocalTime
                                LocalTime selectedTime = LocalTime.of(selectedHour, selectedMinute);

                                // Format as HH:mm and display
                                String formattedTime = DateUtil.localTimeToString(selectedTime);
                                tvSelectedTime.setText(formattedTime);

                                // Save to model
                                activity.setTime(DateUtil.localTimeToLong(selectedTime));

                                // Clear any previous error
                                tvSelectedTime.setError(null);
                            }
                        },
                        hour,
                        minute,
                        true
                );

                timePickerDialog.show();
            }
        });


        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validator.validate()) return;

                String selectedTime = tvSelectedTime.getText().toString().trim();
                int selectedActionPosition = actionSpinner.getSelectedItemPosition();

                // Validate selected time
                if (selectedTime.isEmpty() || selectedTime.equalsIgnoreCase("Select time") || selectedTime.equalsIgnoreCase("No time selected")) {
                    tvSelectedTime.setError("Time is required");
                    tvSelectedTime.requestFocus();
                    return;
                }

                // Validate selected activity (spinner)
                if (selectedActionPosition == 0) {
                    TextView errorText = (TextView) actionSpinner.getSelectedView();
                    if (errorText != null) {
                        errorText.setError("Select a valid activity");
                        errorText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                    actionSpinner.requestFocus();
                    return;
                }

                // All valid - proceed to save
                activity.setDate(System.currentTimeMillis());
                activity.setDetails(etNote.getText().toString());
                activity.setAction(Action.valueOf(actionSpinner.getSelectedItem().toString()));
                String babyId = sharedPreferences.getString("selectedBabyIdFs", null);
                activity.setBabyId(babyId);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity.getTime() == 0L) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime parsedTime = LocalTime.parse(selectedTime, formatter);
                        activity.setTime(DateUtil.localTimeToLong(parsedTime));
                    } catch (Exception e) {
                        tvSelectedTime.setError("Invalid time format");
                        tvSelectedTime.requestFocus();
                        return;
                    }
                }

                if (btnAddNote.getText().equals("Update"))
                    viewModel.update(activity);
                else
                    viewModel.add(activity);

                new Handler().postDelayed(() -> finish(), 1500);
            }
        });



        btnCancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Closes the activity without saving
                finish();
            }
        });
    }

    // Initializes the ViewModel
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
    }

    // Sets up the spinner with available activity types
    protected void setSpinner(Action selectedAction) {
        if (actionSpinner == null) {
            actionSpinner = findViewById(R.id.spActions);
        }

        List<String> typeList = new ArrayList<>();
        typeList.add("Select activity");

        for (Action action : Action.values()) {
            typeList.add(action.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter);

        if (selectedAction != null) {
            int index = typeList.indexOf(selectedAction.name());
            if (index >= 0) {
                actionSpinner.setSelection(index);
            }
        } else {
            actionSpinner.setSelection(0);
        }
    }


    @Override
    public void setValidation() {
        Validator.clear();
        Validator.add(new TextRule(etNote, RuleOperation.REQUIRED, "Note is required"));
    }

    @Override
    public boolean validate() {
        return Validator.validate();
    }

}
