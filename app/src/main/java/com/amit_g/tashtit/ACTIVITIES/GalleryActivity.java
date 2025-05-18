package com.amit_g.tashtit.ACTIVITIES;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit_g.helper.AlertUtil;
import com.amit_g.helper.BitMapHelper;
import com.amit_g.helper.DateUtil;
import com.amit_g.helper.Global;
import com.amit_g.model.Baby;
import com.amit_g.model.Galleries;
import com.amit_g.model.Gallery;
import com.amit_g.model.Gender;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.ADPTERS.GalleryAdapter;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.GalleryViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import kotlinx.coroutines.GlobalScope;

public class GalleryActivity extends BaseActivity {
    private ExtendedFloatingActionButton fabAddPhoto;
    private RecyclerView rvGallery;
    private GalleryViewModel viewModel;
    private GalleryAdapter adapter;
    private ActivityResultLauncher<Void>   cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Gallery gallery;
    private String babyId;
    private Bitmap bitmapPhoto;



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

    @Override
    protected void initializeViews() {
        fabAddPhoto = findViewById(R.id.fabAddPhoto);
        rvGallery = findViewById(R.id.rvGallery);
    }

    @Override
    protected void setListeners() {
        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.takePicture(GalleryActivity.this, cameraLauncher, galleryLauncher, requestPermissionLauncher);
            }
        });
    }
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
                        } catch (Exception e) {
                            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Launch appropriate action based on currentRequestType
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

    protected void setRecyclerView(){
        adapter = new GalleryAdapter(null, R.layout.single_gallery_layout, holder -> {
            holder.putView("picture",holder.itemView.findViewById(R.id.imgBabyGallery));
            holder.putView("date",holder.itemView.findViewById(R.id.tvImageDate));
        }
                ,((holder, item, position) -> {
            ((ImageView)holder.getView("picture")).setImageBitmap(BitMapHelper.stringToBitmap(item.getPicture()));
            ((TextView)holder.getView("date")).setText(DateUtil.longDateToString(item.getDate()));
        }));
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        babyId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("selectedBabyIdFs", null);
        viewModel.getPicturesForBabyId(babyId).observe(this, new Observer<List<Gallery>>() {
            @Override
            public void onChanged(List<Gallery> galleries) {
                Collections.sort(galleries, (p1, p2) -> Long.compare(p2.getDate(), p1.getDate()));
                adapter.setItems(galleries);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewModel();
    }
}