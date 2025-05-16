package com.amit_g.tashtit.ACTIVITIES;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.ProgressAdapter;
import com.amit_g.tashtit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProgressActivity extends BaseActivity {

    private RecyclerView rvProgress;
    private FloatingActionButton fabAddProgress;
    private ProgressAdapter adapter;


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
    }

    @Override
    protected void initializeViews() {
        rvProgress = findViewById(R.id.rvProgress);
        fabAddProgress = findViewById(R.id.fabAddProgress);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void setViewModel() {

    }
}