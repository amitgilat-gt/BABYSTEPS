package com.amit_g.tashtit.ACTIVITIES;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.AlertUtil;
import com.amit_g.helper.BitMapHelper;
import com.amit_g.helper.DateUtil;
import com.amit_g.helper.Global;
import com.amit_g.model.Galleries;
import com.amit_g.model.Gallery;
import com.amit_g.model.btnNavigation;
import com.amit_g.model.btnNavigations;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;
import com.amit_g.tashtit.ADPTERS.GalleryAdapter;
import com.amit_g.tashtit.ADPTERS.NevigationAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.GalleryViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;

public class GalleryActivity extends BaseActivity {

    // Handles photo addition from camera/gallery, deletion, and navigation menu
    private ExtendedFloatingActionButton fabAddPhoto;
    private RecyclerView rvGallery;
    private GalleryViewModel viewModel;
    private GalleryAdapter adapter;
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Gallery gallery;
    private String babyId;
    private Bitmap bitmapPhoto;
    private RecyclerView rvGalleryMenu;
    private NevigationAdapter menuAdapter;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);
        initializeViews();
        setRecyclerView();
        setViewModel();
        setLaunchers();
        setListeners();
    }

    // Initializes views from the layout
    @Override
    protected void initializeViews() {
        fabAddPhoto = findViewById(R.id.fabAddPhoto);
        rvGallery = findViewById(R.id.rvGallery);
        rvGalleryMenu = findViewById(R.id.rvGalleryMenu);
        setupGalleryMenu();
    }

    // Sets click listeners for adding and deleting gallery photos
    @Override
    protected void setListeners() {
        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.takePicture(GalleryActivity.this, cameraLauncher, galleryLauncher, requestPermissionLauncher);
            }
        });
        adapter.setOnItemLongClickListener(new GenericAdapter.OnItemLongClickListener<Gallery>() {
            @Override
            public boolean onItemLongClick(Gallery item, int position) {
                new AlertDialog.Builder(GalleryActivity.this)
                        .setTitle("Delete Photo")
                        .setMessage("Are you sure you want to delete this photo?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.delete(item);
                            setViewModel();
                            Toast.makeText(GalleryActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    // Initializes launchers for camera, gallery, and permission requests
    private void setLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitMap -> {
                    if (bitMap != null) {
                        bitmapPhoto = bitMap;
                        Gallery gallery = new Gallery();
                        gallery.setPicture(BitMapHelper.bitmapToString(bitmapPhoto));
                        gallery.setDate(System.currentTimeMillis());
                        gallery.setBabyId(getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null));
                        viewModel.add(gallery);
                        setViewModel();
                    } else {
                        Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT).show();
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        final Uri imageUri = result.getData().getData();
                        try {
                            bitmapPhoto = BitMapHelper.uriToBitmap(imageUri, GalleryActivity.this);
                            gallery = new Gallery();
                            gallery.setPicture(BitMapHelper.bitmapToString(bitmapPhoto));
                            gallery.setDate(System.currentTimeMillis());
                            viewModel.add(gallery);
                            setViewModel();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        if (Global.getCurrentRequestType() == 0) {
                            cameraLauncher.launch(null);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            galleryLauncher.launch(intent);
                        }
                    } else {
                        AlertUtil.alertOk(GalleryActivity.this, "Permission required", "Permission required to access camera/gallery", true, 0);
                    }
                });
    }

    // Sets up the gallery RecyclerView
    protected void setRecyclerView() {
        adapter = new GalleryAdapter(null, R.layout.single_gallery_layout, holder -> {
            holder.putView("picture",holder.itemView.findViewById(R.id.imgBabyGallery));
            holder.putView("date",holder.itemView.findViewById(R.id.tvImageDate));
        }, ((holder, item, position) -> {
            ((ImageView)holder.getView("picture")).setImageBitmap(BitMapHelper.stringToBitmap(item.getPicture()));
            ((TextView)holder.getView("date")).setText(DateUtil.longDateToString(item.getDate()));
        }));
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new LinearLayoutManager(this));
    }

    // Sets and observes data from the ViewModel
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        babyId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null);

        viewModel.listenToPicturesForBabyId(babyId).observe(this, new Observer<Galleries>() {
            @Override
            public void onChanged(Galleries galleries) {
                if (galleries != null && !galleries.isEmpty()) {
                    Collections.sort(galleries, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                    adapter.setItems(galleries);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Refreshes data when returning to activity
    @Override
    protected void onResume() {
        super.onResume();
        setViewModel();
    }

    // Initializes the horizontal navigation menu
    private void setupGalleryMenu() {
        btnNavigations navList = new btnNavigations();
        navList.add(new btnNavigation("Home", HomeActivity.class));
        navList.add(new btnNavigation("Measurements", ProgressActivity.class));
        navList.add(new btnNavigation("Last Activities", AllActivitiesActivity.class));
        navList.add(new btnNavigation("Add Baby", ActivityBabySign.class));
        navList.add(new btnNavigation("Connect To Baby", ConnectToBabyActivity.class));
        navList.add(new btnNavigation("Log Out", LoginActivity.class));

        menuAdapter = new NevigationAdapter(navList, R.layout.single_button_layout, holder -> {
            holder.putView("btnNev", holder.itemView.findViewById(R.id.btnNev));
        }, (holder, item, position) -> {
            Button button = (Button) holder.getView("btnNev");
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                if ("Log Out".equals(item.getLabel())) {
                    new AlertDialog.Builder(GalleryActivity.this)
                            .setTitle("Log Out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .clear()
                                        .apply();
                                Intent intent = new Intent(GalleryActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finishAffinity();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    startActivity(new Intent(GalleryActivity.this, item.getTargetActivity()));
                }
            });

        });

        rvGalleryMenu.setAdapter(menuAdapter);
        rvGalleryMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}
