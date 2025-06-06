package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.DateUtil;
import com.amit_g.model.Baby;
import com.amit_g.model.LastActivity;
import com.amit_g.model.Progress;
import com.amit_g.model.btnNavigation;
import com.amit_g.model.btnNavigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.ProgressViewModel;
import com.amit_g.viewmodel.UserBabyViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends BaseActivity {

    // UI components and view models
    private RecyclerView buttonRecyclerView;
    private NevigationAdapter adapter;
    private SharedPreferences sharedPreferences;
    private TextView welcomeText;
    private ConstraintLayout clBabySp;
    private Spinner spBaby;
    private UserBabyViewModel userBabyViewModel;
    private BabiesViewModel babiesViewModel;
    private String userId;
    private View lastActivitySection;
    private TextView activityType;
    private TextView activityTime;
    private TextView activityDate;
    private TextView activityDetails;
    private ActivityViewModel activityViewModel;
    private List<Baby> babyList = new ArrayList<>();
    private ProgressViewModel progressViewModel;
    private View lastProgressSection;
    private TextView progressDate;
    private TextView progressWeight;
    private TextView progressHeight;
    private String selectedBabyIdFs;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setViewModel();
        setListeners();
    }

    // Initializes views and loads user info and navigation buttons
    @Override
    protected void initializeViews() {
        buttonRecyclerView = findViewById(R.id.buttonRecyclerView);
        welcomeText = findViewById(R.id.welcomeText);
        clBabySp = findViewById(R.id.clBabySp);
        spBaby = findViewById(R.id.spBaby);
        lastActivitySection = findViewById(R.id.lastActivitySection);
        activityType = lastActivitySection.findViewById(R.id.activityTypeText);
        activityTime = lastActivitySection.findViewById(R.id.timeText);
        activityDate = lastActivitySection.findViewById(R.id.dateText);
        activityDetails = lastActivitySection.findViewById(R.id.actionTextView);
        lastProgressSection = findViewById(R.id.lastProgressSection);
        progressDate = lastProgressSection.findViewById(R.id.tvDate);
        progressWeight = lastProgressSection.findViewById(R.id.tvWeight);
        progressHeight = lastProgressSection.findViewById(R.id.tvHeight);

        btnNavigations navList = new btnNavigations();
        navList.add(new btnNavigation("Measurements", ProgressActivity.class));
        navList.add(new btnNavigation("Gallery", GalleryActivity.class));
        navList.add(new btnNavigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNavigation("Add Baby", ActivityBabySign.class));
        navList.add(new btnNavigation("Connect To Baby", ConnectToBabyActivity.class));
        navList.add(new btnNavigation("Log Out", LoginActivity.class));

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userIdFs", null);
        String username = sharedPreferences.getString("username", null);
        welcomeText.setText("Hi, " + username);

        setRecyclerView(navList);
    }

    // Sets up horizontal navigation button list
    protected void setRecyclerView(btnNavigations navList) {
        adapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            Button button = (Button) holder.getView("btnNev");
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                if (item.getTargetActivity().equals(LoginActivity.class)) {
                    new android.app.AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Log out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                sharedPreferences.edit().clear().apply();
                                Intent intent = new Intent(HomeActivity.this, item.getTargetActivity());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finishAffinity();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    startActivity(new Intent(HomeActivity.this, item.getTargetActivity()));
                }
            });
        });
        buttonRecyclerView.setAdapter(adapter);
        buttonRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    // Sets listener for baby selection spinner
    @Override
    protected void setListeners() {
        spBaby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < babyList.size()) {
                    selectedBabyIdFs = babyList.get(position).getIdFs();
                    sharedPreferences.edit().putString("selectedBabyIdFs", selectedBabyIdFs).apply();
                    observeLiveActivityData(selectedBabyIdFs);
                    observeLiveProgressData(selectedBabyIdFs);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    // Initializes view models and fetches user/baby data
    @Override
    protected void setViewModel() {
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);
        babiesViewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        progressViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        if (userId != null) {
            userBabyViewModel.getBabiesForUserId(userId).observe(this, babies -> {
                if (babies != null && !babies.isEmpty()) {
                    babyList = new ArrayList<>(babies);
                    List<String> babyNames = new ArrayList<>();
                    for (Baby baby : babyList) {
                        babyNames.add(baby.getName() != null ? baby.getName() : "Unknown Baby");
                    }
                    updateSpinner(babyNames);
                    String savedBabyId = sharedPreferences.getString("selectedBabyIdFs", null);
                    if (savedBabyId != null) {
                        for (int i = 0; i < babyList.size(); i++) {
                            if (savedBabyId.equals(babyList.get(i).getIdFs())) {
                                spBaby.setSelection(i);
                                observeLiveActivityData(selectedBabyIdFs);
                                observeLiveProgressData(selectedBabyIdFs);

                                break;
                            }
                        }
                    } else if (!babyList.isEmpty()) {
                        String defaultBabyId = babyList.get(0).getIdFs();
                        observeLiveActivityData(selectedBabyIdFs);
                        observeLiveProgressData(selectedBabyIdFs);

                    }
                } else {
                    updateSpinnerWithNoData();
                    updateActivityUI(null);
                    updateProgressUI(null);
                }
            });
        } else {
            updateSpinnerWithNoData();
            updateActivityUI(null);
            updateProgressUI(null);
        }
    }

    private void observeLiveActivityData(String babyIdFs) {
        activityViewModel.listenToActivitiesForBabyId(babyIdFs).observe(this, activities -> {
            if (activities != null && !activities.isEmpty()) {
                Collections.sort(activities, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                updateActivityUI(activities.get(0));
            } else {
                updateActivityUI(null);
            }
        });
    }

    private void observeLiveProgressData(String babyIdFs) {
        progressViewModel.listenToProgressForBabyId(babyIdFs).observe(this, progressList -> {
            if (progressList != null && !progressList.isEmpty()) {
                Collections.sort(progressList, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                updateProgressUI(progressList.get(0));
            } else {
                updateProgressUI(null);
            }
        });
    }


    // Updates the progress section UI
    private void updateProgressUI(Progress progress) {
        if (progress != null) {
            progressDate.setText("Date: " + DateUtil.longDateToString(progress.getDate()));
            progressWeight.setText("Weight: " + progress.getWeight() + " kg");
            progressHeight.setText("Height: " + progress.getHeight() + " cm");
            lastProgressSection.setVisibility(View.VISIBLE);
        } else {
            progressDate.setText("No progress yet");
            progressWeight.setText("");
            progressHeight.setText("");
            lastProgressSection.setVisibility(View.VISIBLE);
        }
    }

    // Updates the activity section UI
    private void updateActivityUI(LastActivity lastActivity) {
        if (lastActivity != null) {
            activityType.setText(lastActivity.getAction() != null ? lastActivity.getAction().toString() : "N/A");
            activityTime.setText(DateUtil.longToLocalTime(lastActivity.getTime()).toString());
            activityDate.setText(DateUtil.longDateToString(lastActivity.getDate()));
            activityDetails.setText(lastActivity.getDetails() != null ? lastActivity.getDetails() : "No details");
            lastActivitySection.setVisibility(View.VISIBLE);
        } else {
            activityType.setText("No activity yet");
            activityTime.setText("");
            activityDate.setText("");
            activityDetails.setText("");
            lastActivitySection.setVisibility(View.VISIBLE);
        }
    }

    // Populates spinner with baby names
    private void updateSpinner(List<String> babyNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                babyNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaby.setAdapter(adapter);
    }

    // Called when returning to the activity
    @Override
    protected void onResume() {
        super.onResume();
        setViewModel();
        String selectedBabyIdFs = sharedPreferences.getString("selectedBabyIdFs", null);
        if (selectedBabyIdFs != null) {
            observeLiveActivityData(selectedBabyIdFs);
            observeLiveProgressData(selectedBabyIdFs);
        }
    }

    // Sets spinner with default message if no baby data exists
    private void updateSpinnerWithNoData() {
        List<String> emptyList = Collections.singletonList("No babies enrolled");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                emptyList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaby.setAdapter(adapter);
    }
}

