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

public class ActivitiesActivity extends BaseActivity {
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
    //private LocalTime selectedTime;

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
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        setListeners();
    }

    @Override
    protected void initializeViews() {
        etNote = findViewById(R.id.etNote);
        actionSpinner = findViewById(R.id.spActions);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnCancelNote = findViewById(R.id.btnCancelNote);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        textView2 = findViewById(R.id.textView2);

        // Get passed activity (if editing)
        LastActivity passedActivity = (LastActivity) getIntent().getSerializableExtra("activity");
        if (passedActivity != null) {
            activity = passedActivity;
            btnAddNote.setText("Update");
            textView2.setText("Update Activity");
            etNote.setText(activity.getDetails());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity.getTime() != 0L) {
                tvSelectedTime.setText(DateUtil.localTimeToString(DateUtil.longToLocalTime(activity.getTime())));
            }

            setSpinner(activity.getAction()); // call AFTER setting views
        } else {
            activity = new LastActivity();
            setSpinner(null); // fresh form
        }
    }


    @Override
    protected void setListeners() {
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current time as default values for the picker
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create and show the TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ActivitiesActivity.this,
                        (view, selectedHour, selectedMinute) -> {
                            // Format the selected time as HH:mm and set to TextView
                            String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                            tvSelectedTime.setText(time);

                            // Parse the time string to LocalTime
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalTime selectedTime = LocalTime.of(selectedHour, selectedMinute);
                                tvSelectedTime.setText(DateUtil.localTimeToString(selectedTime));
                                activity.setTime(DateUtil.localTimeToLong(selectedTime));
                            }

                        },
                        hour,
                        minute,
                        true // true = 24-hour format, false = AM/PM format
                );

                timePickerDialog.show();
            }
        });


        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDate(System.currentTimeMillis());
                activity.setDetails(etNote.getText().toString());
                activity.setAction(Action.valueOf(actionSpinner.getSelectedItem().toString()));
                String babyId = sharedPreferences.getString("selectedBabyIdFs", null);
                activity.setBabyId(babyId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity.getTime() == 0L) {
                    LocalTime parsedTime = LocalTime.parse(tvSelectedTime.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"));
                    activity.setTime(DateUtil.localTimeToLong(parsedTime));
                }


                viewModel.save(activity);
                new Handler().postDelayed(() -> finish(), 1500);
            }
        });

        btnCancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
    }
    protected void setSpinner(Action selectedAction) {
        if (actionSpinner == null) {
            actionSpinner = findViewById(R.id.spActions);
        }

        List<String> typeList = new ArrayList<>();
        typeList.add("Select activity"); // Placeholder

        for (Action action : Action.values()) {
            typeList.add(action.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter);

        // Set selection if editing
        if (selectedAction != null) {
            int index = typeList.indexOf(selectedAction.name());
            if (index >= 0) {
                actionSpinner.setSelection(index);
            }
        } else {
            actionSpinner.setSelection(0); // Default to "Select activity"
        }
    }


}