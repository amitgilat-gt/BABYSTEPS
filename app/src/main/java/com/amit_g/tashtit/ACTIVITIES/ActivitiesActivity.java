package com.amit_g.tashtit.ACTIVITIES;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.model.Action;
import com.amit_g.model.LastActivity;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesActivity extends BaseActivity {
    private EditText etNote;
    private Spinner actionSpinner;
    private Button btnAddNote;
    private Button btnCancelNote;
    private ActivityViewModel viewModel;
    private LastActivity activity;

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
        initializeViews();
        setSpinner();
        setViewModel();
        setListeners();
    }

    @Override
    protected void initializeViews() {
        etNote = findViewById(R.id.etNote);
        actionSpinner = findViewById(R.id.spActions);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnCancelNote = findViewById(R.id.btnCancelNote);
    }

    @Override
    protected void setListeners() {
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = new LastActivity();
                activity.setDate(System.currentTimeMillis());
                activity.setDetails(etNote.getText().toString());
                activity.setAction(Action.valueOf(actionSpinner.getSelectedItem().toString()));
                viewModel.add(activity);
            }
        });
        btnCancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
    }
    protected void setSpinner(){
        if (actionSpinner == null) {
            actionSpinner = findViewById(R.id.spActions);
        }

        List<String> typeList = new ArrayList<>();
        typeList.add("Select action");
        for (Action action : Action.values()) {
            typeList.add(action.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter);
    }
}