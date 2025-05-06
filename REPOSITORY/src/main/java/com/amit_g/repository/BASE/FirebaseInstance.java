package com.amit_g.repository.BASE;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInstance {
    private static volatile FirebaseInstance _instance = null;
    public static FirebaseApp app;

    private FirebaseInstance(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("babysteps-dfb4c")
                .setApplicationId("babysteps-dfb4c")
                .setApiKey("AIzaSyAJePWqMxZGDk3AY8I_hWluw4gG0EqWeRo")
                .setStorageBucket("babysteps-dfb4c.firebasestorage.app")
                .build();

        app = FirebaseApp.initializeApp(context, options);
    }

    public static FirebaseInstance instance(Context context) {
        if (_instance == null) {  // 1st check
            synchronized (FirebaseInstance.class) {
                if (_instance == null){ // 2nd check
                    _instance = new FirebaseInstance(context);
                }
            }
        }

        return _instance;
    }
}
