package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.model.Baby;
import com.amit_g.model.btnNevigation;
import com.amit_g.model.btnNevigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.UserBabyViewModel;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    protected void initializeViews() {
        buttonRecyclerView = findViewById(R.id.buttonRecyclerView);
        welcomeText = findViewById(R.id.welcomeText);
        clBabySp = findViewById(R.id.clBabySp);
        spBaby = findViewById(R.id.spBaby);

        btnNevigations navList = new btnNevigations();
        navList.add(new btnNevigation("Growth", GrowthActivity.class));
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
        // Optional: Add any listeners if needed
    }

    @Override
    protected void setViewModel() {
        userBabyViewModel = new ViewModelProvider(this).get(UserBabyViewModel.class);
        babiesViewModel = new ViewModelProvider(this).get(BabiesViewModel.class);

        // Using the repository method that directly returns Baby objects
        if (userId != null) {
            userBabyViewModel.getBabiesForUserId(userId).observe(this, new Observer<List<Baby>>() {
                @Override
                public void onChanged(List<Baby> babies) {
                    if (babies != null && !babies.isEmpty()) {
                        // Extract baby names from Baby objects
                        List<String> babyNames = new ArrayList<>();
                        for (Baby baby : babies) {
                            String babyName = baby.getName();
                            babyNames.add(babyName != null ? babyName : "Unknown Baby");
                        }
                        updateSpinner(babyNames);
                    } else {
                        // No babies connected
                        updateSpinnerWithNoData();
                    }
                }
            });
        } else {
            updateSpinnerWithNoData();
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