package com.amit_g.tashtit.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.DateUtil;
import com.amit_g.model.AllProgress;
import com.amit_g.model.btnNevigation;
import com.amit_g.model.btnNevigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.ADPTERS.ProgressAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ProgressViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

public class ProgressActivity extends BaseActivity {

    private RecyclerView rvProgress;
    private FloatingActionButton fabAddProgress;
    private ProgressAdapter adapter;
    private ProgressViewModel viewModel;
    private RecyclerView rvMenuProgress;
    private NevigationAdapter nevAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setRecyclerView();
        setListeners();
    }

    private void setRecyclerView() {
        adapter = new ProgressAdapter(null, R.layout.single_progress_layout,holder -> {
            holder.putView("date", holder.itemView.findViewById(R.id.tvDate));
            holder.putView("weight", holder.itemView.findViewById(R.id.tvWeight));
            holder.putView("height", holder.itemView.findViewById(R.id.tvHeight));
        }, (holder, item, position) -> {
            ((TextView) holder.getView("date")).setText(DateUtil.longDateToString(item.getDate()));
            ((TextView) holder.getView("weight")).setText("Weight: " + String.valueOf(item.getWeight()));
            ((TextView) holder.getView("height")).setText("Height: " + String.valueOf(item.getHeight()));
        });
        rvProgress.setAdapter(adapter);
        rvProgress.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void initializeViews() {
        rvProgress = findViewById(R.id.rvProgress);
        fabAddProgress = findViewById(R.id.fabAddProgress);
        rvMenuProgress = findViewById(R.id.rvMenuProgress);
        btnNevigations navList = new btnNevigations();
        navList.add(new btnNevigation("Growth", ProgressActivity.class));
        navList.add(new btnNevigation("Gallery", GalleryActivity.class));
        navList.add(new btnNevigation("User", UserActivity.class));
        navList.add(new btnNevigation("Baby Sign", ActivityBabySign.class));
        navList.add(new btnNevigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNevigation("Baby Connect", ConnectToBabyActivity.class));
        navList.add(new btnNevigation("Home", HomeActivity.class));
        setRecyclerView2(navList);
    }

    private void setRecyclerView2(btnNevigations navList) {
        nevAdapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            TextView button = (TextView) holder.getView("btnNev");
            button.setText(item.getLabel());
            button.setOnClickListener(v -> {
                startActivity(new Intent(ProgressActivity.this, item.getTargetActivity()));
            });
        });
        rvMenuProgress.setAdapter(nevAdapter);
        rvMenuProgress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    protected void setListeners() {
        fabAddProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(GrowthActivity.class);
            }
        });
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        String babyId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null);

        if (babyId != null) {
            viewModel.getProgressForBabyId(babyId).observe(this, progresses -> {
                if (progresses != null) {
                    // Sort by date descending
                    Collections.sort(progresses, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));

                    adapter.setItems(progresses);
                    adapter.notifyDataSetChanged();
                } else {
                    // handle empty or error case here
                }
            });

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setViewModel(); // Refresh baby list every time you return to HomeActivity
    }

}