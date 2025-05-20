package com.amit_g.tashtit.ACTIVITIES.BASE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.amit_g.tashtit.ACTIVITIES.ActivityBabySign;
import com.amit_g.tashtit.ACTIVITIES.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.amit_g.tashtit.ACTIVITIES.MainActivity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amit_g.tashtit.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setMenu();
    }

    protected abstract void initializeViews();
    protected abstract void setListeners();
    protected abstract void setViewModel();
    //public static Member currentMember = null;

    //region Progress Dialog
    public ProgressDialog mProgressDialog;
    public void showProgressDialog(String title, @NonNull String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            if (title != null)
                mProgressDialog.setTitle(title);
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    //endregion

    //region NAV_BAR
    protected BottomNavigationView bottomNavigationView;

    private void setMenu(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home){
                    navigateToActivity(MainActivity.class);
                }
                else if(itemId == R.id.navigation_members){
                    navigateToActivity(ActivityBabySign.class);
                }
                else if(itemId == R.id.navigation_cities){
                    navigateToActivity(RegisterActivity.class);
                }
                else{

                }
                return false;
            }
        });
    }

    protected void setSelectedNavigationItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    protected void navigateToActivity(Class<?> activityClass) {
        // Check if we're not already in the target activity
        if (activityClass == null){
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!this.getClass().equals(activityClass)) {
                Intent intent = new Intent(this, activityClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
    }

    //endregion
}