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
import com.amit_g.model.btnNevigation;
import com.amit_g.model.btnNevigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.UserBabyViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends BaseActivity {

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
        setViewModel(); // Set up ViewModels
        setListeners();
    }

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

        btnNevigations navList = new btnNevigations();
        navList.add(new btnNevigation("Growth", ProgressActivity.class));
        navList.add(new btnNevigation("Gallery", GalleryActivity.class));
        navList.add(new btnNevigation("User", UserActivity.class));
        navList.add(new btnNevigation("Baby Sign", ActivityBabySign.class));
        navList.add(new btnNevigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNevigation("Baby Connect", ConnectToBabyActivity.class));

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userIdFs", null);
        String username = sharedPreferences.getString("username", null);
        welcomeText.setText("Hi, " + username);

        setRecyclerView(navList);
    }

    protected void setRecyclerView(btnNevigations navList) {
        adapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            Button button = (Button) holder.getView("btnNev");
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                startActivity(new Intent(HomeActivity.this, item.getTargetActivity()));
            });
        });
        buttonRecyclerView.setAdapter(adapter);
        buttonRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void setListeners() {
        spBaby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < babyList.size()) {
                    String selectedBabyIdFs = babyList.get(position).getIdFs();
                    sharedPreferences.edit().putString("selectedBabyIdFs", selectedBabyIdFs).apply();

                    // Fetch all activities for the selected baby and update UI with latest
                    fetchAndDisplayLatestActivity(selectedBabyIdFs);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void fetchAndDisplayLatestActivity(String babyIdFs) {
        activityViewModel.getActivitiesForBabyId(babyIdFs).observe(this, activities -> {
            if (activities != null && !activities.isEmpty()) {
                Collections.sort(activities, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));  // Sort descending by date timestamp
                LastActivity latest = activities.get(0);
                updateActivityUI(latest);
            } else {
                updateActivityUI(null);
            }
        });
    }


    @Override
    protected void setViewModel() {
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);
        babiesViewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        if (userId != null) {
            userBabyViewModel.getBabiesForUserId(userId).observe(this, babies -> {
                if (babies != null && !babies.isEmpty()) {
                    babyList = new ArrayList<>(babies);

                    List<String> babyNames = new ArrayList<>();
                    for (Baby baby : babyList) {
                        babyNames.add(baby.getName() != null ? baby.getName() : "Unknown Baby");
                    }

                    updateSpinner(babyNames);

                    // Set default selection (e.g. previously selected baby)
                    String savedBabyId = sharedPreferences.getString("selectedBabyIdFs", null);
                    if (savedBabyId != null) {
                        for (int i = 0; i < babyList.size(); i++) {
                            if (savedBabyId.equals(babyList.get(i).getIdFs())) {
                                spBaby.setSelection(i);
                                // Instead of ViewModel handling latest activity, fetch and display locally
                                fetchAndDisplayLatestActivity(savedBabyId);
                                break;
                            }
                        }
                    } else if (!babyList.isEmpty()) {
                        // Select first baby and fetch activities
                        fetchAndDisplayLatestActivity(babyList.get(0).getIdFs());
                    }
                } else {
                    updateSpinnerWithNoData();
                    updateActivityUI(null);
                }
            });
        } else {
            updateSpinnerWithNoData();
            updateActivityUI(null);
        }
    }

    // Separate method to update the UI with activity data
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

    private void updateSpinner(List<String> babyNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                babyNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaby.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selectedBabyIdFs = sharedPreferences.getString("selectedBabyIdFs", null);
        if (selectedBabyIdFs != null) {
            // Instead of ViewModel setter, directly fetch activities
            fetchAndDisplayLatestActivity(selectedBabyIdFs);
        }
    }

    private void updateSpinnerWithNoData() {
        List<String> emptyList = Collections.singletonList("No babies connected");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                emptyList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaby.setAdapter(adapter);
    }
}
