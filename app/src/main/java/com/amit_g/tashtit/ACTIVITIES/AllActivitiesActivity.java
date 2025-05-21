package com.amit_g.tashtit.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.DateUtil;
import com.amit_g.model.LastActivity;
import com.amit_g.model.btnNavigation;
import com.amit_g.model.btnNavigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.ActivitiesAdapter;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;
import java.util.Collections;

public class AllActivitiesActivity extends BaseActivity {
    private RecyclerView rvActivities;
    private FloatingActionButton fabAddActivity;
    private ActivityViewModel viewModel;
    private ActivitiesAdapter adapter;
    private RecyclerView menuRecyclerView;
    private NevigationAdapter menuAdapter;
    private String babyId;

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
        btnNavigations navList = new btnNavigations();

        navList.add(new btnNavigation("Home",HomeActivity.class));
        navList.add(new btnNavigation("Measurements", ProgressActivity.class));
        navList.add(new btnNavigation("Gallery", GalleryActivity.class));
        navList.add(new btnNavigation("Add Baby", ActivityBabySign.class));
        navList.add(new btnNavigation("Connect To Baby", ConnectToBabyActivity.class));
        navList.add(new btnNavigation("Log Out", LoginActivity.class));
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
        adapter.setOnItemClickListener(new GenericAdapter.OnItemClickListener<LastActivity>() {
            @Override
            public void onItemClick(LastActivity item, int position) {
                Intent intent = new Intent(AllActivitiesActivity.this, ActivitiesActivity.class);
                intent.putExtra("activity", item);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new GenericAdapter.OnItemLongClickListener<LastActivity>() {
            @Override
            public boolean onItemLongClick(LastActivity item, int position) {
                new AlertDialog.Builder(AllActivitiesActivity.this) // Replace `context` with your Activity or Fragment context
                        .setTitle("Delete Entry")
                        .setMessage("Are you sure you want to delete this activity?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.delete(item);
                            setViewModel();
                            navigateToActivity(AllActivitiesActivity.class);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            }
        });
    }
    protected void setRecyclerView2(btnNavigations navList) {
        menuAdapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            Button button = (Button) holder.getView("btnNev");
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                if ("Log Out".equals(item.getLabel())) {
                    new AlertDialog.Builder(AllActivitiesActivity.this)
                            .setTitle("Log Out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .clear()
                                        .apply();
                                Intent intent = new Intent(AllActivitiesActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finishAffinity();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    startActivity(new Intent(AllActivitiesActivity.this, item.getTargetActivity()));
                }
            });

        });

        menuRecyclerView.setAdapter(menuAdapter);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        babyId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null);
        viewModel.getActivitiesForBabyId(babyId).observe(this, activities -> {
            if (activities != null && !activities.isEmpty()) {
                Collections.sort(activities, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                adapter.setItems(activities);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setViewModel();
        adapter.notifyDataSetChanged();
    }
}