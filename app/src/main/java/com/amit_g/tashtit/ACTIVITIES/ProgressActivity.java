package com.amit_g.tashtit.ACTIVITIES;

import android.os.Bundle;
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
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.ProgressAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.ProgressViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProgressActivity extends BaseActivity {

    private RecyclerView rvProgress;
    private FloatingActionButton fabAddProgress;
    private ProgressAdapter adapter;
    private ProgressViewModel viewModel;


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
    }

    private void setRecyclerView() {
        adapter = new ProgressAdapter(null, R.layout.single_progress_layout,holder -> {
            holder.putView("date", holder.itemView.findViewById(R.id.tvDate));
            holder.putView("weight", holder.itemView.findViewById(R.id.tvWeight));
            holder.putView("height", holder.itemView.findViewById(R.id.tvHeight));
        }, (holder, item, position) -> {
            ((TextView) holder.getView("date")).setText(DateUtil.longDateToString(item.getDate()));
            ((TextView) holder.getView("weight")).setText(String.valueOf(item.getWeight()));
            ((TextView) holder.getView("height")).setText(String.valueOf(item.getHeight()));
        });
        rvProgress.setAdapter(adapter);
        rvProgress.setLayoutManager(new LinearLayoutManager(this));
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
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        viewModel.getAll();
        viewModel.getLiveDataCollection().observe(this, new Observer<AllProgress>() {
            @Override
            public void onChanged(AllProgress progresses) {
                adapter.setItems(progresses);
            }
        });
    }
}