package com.amit_g.tashtit.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.amit_g.dto.R1Request;
import com.amit_g.dto.R1Response;
import com.amit_g.helper.DateUtil;
import com.amit_g.model.Baby;
import com.amit_g.model.Progress;
import com.amit_g.model.btnNavigation;
import com.amit_g.model.btnNavigations;
import com.amit_g.network.OpenRouterApi;
import com.amit_g.network.RetrofitClient;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.ADPTERS.ProgressAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.BabiesViewModel;
import com.amit_g.viewmodel.ProgressViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressActivity extends BaseActivity {

    // UI components and ViewModels
    private RecyclerView rvProgress;
    private FloatingActionButton fabAddProgress;
    private ProgressAdapter adapter;
    private ProgressViewModel viewModel;
    private RecyclerView rvMenuProgress;
    private NevigationAdapter nevAdapter;
    private Button btnPercentileGenerator;
    private Progress latestProgress = null;
    private BabiesViewModel babiesViewModel;
    private String babyId;
    private TextView tvPercentileResult;

    // Called when activity is created
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

    // Initializes RecyclerView for growth records
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

    // Initializes views and navigation buttons
    @Override
    protected void initializeViews() {
        rvProgress = findViewById(R.id.rvProgress);
        fabAddProgress = findViewById(R.id.fabAddProgress);
        rvMenuProgress = findViewById(R.id.rvMenuProgress);
        btnPercentileGenerator = findViewById(R.id.btnGeneratePercentile);
        tvPercentileResult = findViewById(R.id.tvPercentileResult);
        btnNavigations navList = new btnNavigations();
        navList.add(new btnNavigation("Home", HomeActivity.class));
        navList.add(new btnNavigation("Gallery", GalleryActivity.class));
        navList.add(new btnNavigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNavigation("Add Baby", ActivityBabySign.class));
        navList.add(new btnNavigation("Baby Connect", ConnectToBabyActivity.class));
        navList.add(new btnNavigation("Log Out", LoginActivity.class));
        setRecyclerView2(navList);
    }

    // Sets up horizontal navigation menu
    private void setRecyclerView2(btnNavigations navList) {
        nevAdapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            TextView button = (TextView) holder.getView("btnNev");
            button.setText(item.getLabel());
            button.setOnClickListener(v -> {
                if ("Log Out".equals(item.getLabel())) {
                    new AlertDialog.Builder(ProgressActivity.this)
                            .setTitle("Log Out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .clear()
                                        .apply();
                                Intent intent = new Intent(ProgressActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finishAffinity();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    startActivity(new Intent(ProgressActivity.this, item.getTargetActivity()));
                }
            });
        });
        rvMenuProgress.setAdapter(nevAdapter);
        rvMenuProgress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    // Sets listeners for FAB, percentile generator, and adapter items
    @Override
    protected void setListeners() {
        fabAddProgress.setOnClickListener(v -> navigateToActivity(GrowthActivity.class));

        btnPercentileGenerator.setOnClickListener(v -> {
            if (latestProgress != null) {
                new AlertDialog.Builder(ProgressActivity.this)
                        .setTitle("Generate Percentile")
                        .setMessage("This process might take a while. Are you sure you want to continue?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            calculatePercentiles(latestProgress);
                            showProgressDialog("Generating Percentile", "Please wait...");
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Log.e("Percentile", "No progress data available.");
            }
        });

        adapter.setOnItemLongClickListener((item, position) -> {
            new AlertDialog.Builder(ProgressActivity.this)
                    .setTitle("Delete Measurement")
                    .setMessage("Are you sure you want to delete this measurement?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        viewModel.delete(item);
                        setViewModel();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        adapter.setOnItemClickListener((item, position) -> {
            Intent intent = new Intent(ProgressActivity.this, GrowthActivity.class);
            intent.putExtra("progress", item);
            startActivity(intent);
            setViewModel();
        });
    }

    // Initializes ViewModels and fetches progress data for the selected baby
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        babiesViewModel = new ViewModelProvider(this).get(BabiesViewModel.class);
        babyId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null);

        if (babyId != null) {
            viewModel.listenToProgressForBabyId(babyId).observe(this, progresses -> {
                if (progresses != null && !progresses.isEmpty()) {
                    Collections.sort(progresses, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                    adapter.setItems(progresses);
                    adapter.notifyDataSetChanged();
                    latestProgress = progresses.get(0);
                }
            });
        }
    }

    // Refreshes data when returning to activity
    @Override
    protected void onResume() {
        super.onResume();
        setViewModel();
    }

    // Sends request to calculate height/weight percentiles via external API
    private void calculatePercentiles(Progress progress) {
        if (babyId == null) {
            Log.e("Percentile", "Baby ID is null");
            return;
        }

        babiesViewModel.getBabyById(babyId).addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Log.e("Percentile", "No baby found for id " + babyId);
                return;
            }

            Baby baby = documentSnapshot.toObject(Baby.class);
            if (baby == null) {
                Log.e("Percentile", "Baby data is null");
                return;
            }

            int babyAgeMonths = DateUtil.getAgeInMonths(baby.getBirthDate(), progress.getDate());
            String promptText = "A baby " + (baby.getGender().toString().equalsIgnoreCase("MALE") ? "boy" : "girl") +
                    " is " + babyAgeMonths + " months old, weighs " + progress.getWeight() +
                    " kg and is " + progress.getHeight() +
                    " cm tall. What are his/her height and weight percentiles? Please respond in JSON with keys: height_percentile and weight_percentile.";

            List<R1Request.Message> messages = Collections.singletonList(new R1Request.Message("user", promptText));
            R1Request request = new R1Request("deepseek/deepseek-r1:free", messages);
            OpenRouterApi api = RetrofitClient.getInstance().create(OpenRouterApi.class);

            api.getPercentile(request).enqueue(new Callback<R1Response>() {
                @Override
                public void onResponse(Call<R1Response> call, Response<R1Response> response) {
                    hideProgressDialog();
                    if (!response.isSuccessful()) {
                        Log.e("Percentile", "Unsuccessful response: " + response.code());
                        runOnUiThread(() -> tvPercentileResult.setText("Failed to get response from model."));
                        return;
                    }

                    R1Response r1Response = response.body();
                    if (r1Response == null || r1Response.getChoices() == null || r1Response.getChoices().isEmpty()) {
                        runOnUiThread(() -> tvPercentileResult.setText("Model returned no useful data."));
                        return;
                    }

                    String modelReply = r1Response.getChoices().get(0).getMessage().getContent();
                    modelReply = modelReply.replaceAll("(?s)```(?:json)?\\s*", "").replaceAll("```", "").trim();

                    try {
                        JSONObject json = new JSONObject(modelReply);
                        String heightPercentile = json.optString("height_percentile", "N/A");
                        String weightPercentile = json.optString("weight_percentile", "N/A");

                        runOnUiThread(() -> tvPercentileResult.setText(
                                "Height Percentile: " + heightPercentile + "\n" +
                                        "Weight Percentile: " + weightPercentile
                        ));
                    } catch (JSONException e) {
                        runOnUiThread(() -> tvPercentileResult.setText("Could not parse response as JSON."));
                    }
                }

                @Override
                public void onFailure(Call<R1Response> call, Throwable t) {
                    Log.e("Percentile", "API call failed", t);
                    runOnUiThread(() -> tvPercentileResult.setText("API call failed: " + t.getMessage()));
                }
            });

        }).addOnFailureListener(e -> Log.e("Percentile", "Error fetching baby data", e));
    }
}
