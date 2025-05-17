package com.amit_g.tashtit.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.DateUtil;
import com.amit_g.model.LastActivities;
import com.amit_g.model.btnNevigation;
import com.amit_g.model.btnNevigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.ActivitiesAdapter;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;

public class AllActivitiesActivity extends BaseActivity {
    private RecyclerView rvActivities;
    private FloatingActionButton fabAddActivity;
    private ActivityViewModel viewModel;
    private ActivitiesAdapter adapter;
    private RecyclerView menuRecyclerView;
    private NevigationAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setRecyclerView();
        setViewModel();
        setListeners();

    }

    @SuppressLint("NewApi")
    private void setRecyclerView() {
        adapter = new ActivitiesAdapter(null, R.layout.single_activity_layout, holder -> {
            holder.putView("title", holder.itemView.findViewById(R.id.activityTypeText));
            holder.putView("description", holder.itemView.findViewById(R.id.actionTextView));
            holder.putView("date", holder.itemView.findViewById(R.id.dateText));
            holder.putView("time", holder.itemView.findViewById(R.id.timeText));

        }, (holder, item, position) -> {
            ((TextView) holder.getView("title")).setText(item.getAction().toString());
            ((TextView) holder.getView("description")).setText(item.getDetails());
            ((TextView) holder.getView("date")).setText(DateUtil.formatDate(item.getDate()));

            LocalTime time = DateUtil.longToLocalTime(item.getTime());
            if (time != null) {
                ((TextView) holder.getView("time")).setText(DateUtil.localTimeToString(time));
            } else {
                ((TextView) holder.getView("time")).setText("");
            }
        });
        rvActivities.setAdapter(adapter);
        rvActivities.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void initializeViews() {
        rvActivities = findViewById(R.id.rvProgress);
        fabAddActivity = findViewById(R.id.fabAddProgress);
        menuRecyclerView = findViewById(R.id.rvMenuProgress);
        btnNevigations navList = new btnNevigations();

        navList.add(new btnNevigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNevigation("Home", HomeActivity.class));
        navList.add(new btnNevigation("Growth", ProgressActivity.class));
        navList.add(new btnNevigation("Gallery", GalleryActivity.class));
        navList.add(new btnNevigation("User", UserActivity.class));
        navList.add(new btnNevigation("Baby Sign", ActivityBabySign.class));
        setRecyclerView2(navList);
    }

    @Override
    protected void setListeners() {
        fabAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(ActivitiesActivity.class);
            }
        });
    }
    protected void setRecyclerView2(btnNevigations navList) {
        menuAdapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            Button button = (Button) holder.getView("btnNev");
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                startActivity(new Intent(AllActivitiesActivity.this, item.getTargetActivity()));
            });
        });

        menuRecyclerView.setAdapter(menuAdapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        viewModel.getAll();
        viewModel.getLiveDataCollection().observe(this, new Observer<LastActivities>() {
            @Override
            public void onChanged(LastActivities activities) {
                adapter.setItems(activities);
            }
        });
    }
}