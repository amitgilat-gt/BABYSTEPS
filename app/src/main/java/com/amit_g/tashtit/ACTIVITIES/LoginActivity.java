package com.amit_g.tashtit.ACTIVITIES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.amit_g.model.User;
import com.amit_g.tashtit.ACTIVITIES.BASE.BaseActivity;
import com.amit_g.tashtit.R;
import com.amit_g.viewmodel.UsersViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends BaseActivity {

    // UI components and view model
    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegister;
    private UsersViewModel viewModel;
    private SignInButton btnGoogleSignIn;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setViewModel();
        setListeners();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

    }

    // Initializes view elements from layout
    @Override
    protected void initializeViews() {
        btnLogin   = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPasswordB);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
    }

    // Sets click listeners for login and register actions
    @Override
    protected void setListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Attempt login via ViewModel
                viewModel.loginUser(username, password)
                        .addOnSuccessListener(querySnapshot -> {
                            if (!querySnapshot.isEmpty()) {
                                User user = querySnapshot.getDocuments().get(0).toObject(User.class);
                                saveUserToPreferences(user);
                                navigateToActivity(HomeActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // ⚠️ Handle error
                        });
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(RegisterActivity.class);
            }
        });
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                });
            }
        });
    }

    // Saves logged in user data to shared preferences
    private void saveUserToPreferences(User user) {
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userIdFs", user.getIdFs());
        editor.putString("username", user.getUserName());
        editor.putString("email", user.getEmail());
        editor.remove("selectedBabyIdFs");
        editor.apply();
    }

    // Initializes the ViewModel
    @Override
    protected void setViewModel() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // First, try to retrieve the user from your Firestore based on their Firebase UID
                            viewModel.getUserByDocumentId(uid).addOnSuccessListener(existingUser -> {
                                User userToSave; // Declare a User object that will hold the correct user data

                                if (existingUser == null) {
                                    // User does NOT exist in your Firestore, create a new User object
                                    userToSave = new User();
                                    userToSave.setIdFs(firebaseUser.getUid());
                                    userToSave.setUserName(firebaseUser.getDisplayName());
                                    userToSave.setEmail(firebaseUser.getEmail());
                                    Toast.makeText(this, "New user added to Firestore!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // User already exists in your Firestore, use the existing user data
                                    userToSave = existingUser;
                                    Toast.makeText(this, "Existing user logged in!", Toast.LENGTH_SHORT).show();
                                }

                                viewModel.save(userToSave);
                                saveUserToPreferences(userToSave);

                                // Navigate to the next activity
                                navigateToActivity(HomeActivity.class);

                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, "Error checking or adding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }








}
