package com.amit_g.tashtit.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.model.btnNevigation;
import com.amit_g.model.btnNevigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;

public class HomeActivity extends BaseActivity {

    private RecyclerView buttonRecyclerView;
    private NevigationAdapter adapter;

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
    }

    @Override
    protected void initializeViews() {
        buttonRecyclerView = findViewById(R.id.buttonRecyclerView);

        btnNevigations navList = new btnNevigations();
        navList.add(new btnNevigation("Growth", GrowthActivity.class));
        navList.add(new btnNevigation("Gallery", GalleryActivity.class));
        navList.add(new btnNevigation("User", UserActivity.class));
        navList.add(new btnNevigation("Baby Sign", ActivityBabySign.class));
        navList.add(new btnNevigation("Last Activities", ActivitiesActivity.class));

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
        buttonRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    @Override
    protected void setListeners() {
        // Optional: Add any listeners if needed
    }

    @Override
    protected void setViewModel() {
        // Optional: Add ViewModel bindings if needed
    }
}
