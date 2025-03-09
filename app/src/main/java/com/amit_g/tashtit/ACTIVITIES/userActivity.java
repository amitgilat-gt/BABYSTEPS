package com.amit_g.tashtit.ACTIVITIES;

import static com.amit_g.tashtit.R.id.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;

public class userActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etPassword;
    private EditText etEmail;
    private Button   btnSignUp;
    private CheckBox cbNotification;
    private String userName;
    private String email;
    private String password;
    private boolean notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

    }

    protected void setListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbNotification.isChecked())
                    notifications = true;
                else
                    notifications = false;
                if(etEmail.getText().toString().length()>4)
                    email = etEmail.getText().toString();
                else
                    etEmail.setError("not verified");
                

            }
        });
    }

    @Override
    protected void setViewModel() {

    }

    protected void initializeViews() {
        etUserName     = findViewById(R.id.etUserName);
        etEmail        = findViewById(R.id.etEmail);
        etPassword     = findViewById(R.id.etPassword);
        cbNotification = findViewById(R.id.cbNotification);
        btnSignUp      = findViewById(R.id.btnSignUp);
        setListeners();
    }
}