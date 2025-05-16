package com.amit_g.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amit_g.model.AllProgress;
import com.amit_g.model.Baby;
import com.amit_g.model.Progress;
import com.amit_g.model.UserBaby;
import com.amit_g.model.UsersBabies;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserBabyRepository extends BaseRepository<UserBaby, UsersBabies> {
    public UserBabyRepository(Application application) {
        super(UserBaby.class, UsersBabies.class, application);
    }

    @Override
    protected Query getQueryForExist(UserBaby entity) {
        return getCollection().whereEqualTo("userId", entity.getUserId());
    }
    public LiveData<List<Baby>> getBabiesForUserId(String userId) {
        MutableLiveData<List<Baby>> resultLiveData = new MutableLiveData<>();

        // Step 1: Get all UserBaby docs where userId matches
        getCollection().whereEqualTo("userId", userId).get()
                .addOnSuccessListener(userBabySnapshots -> {
                    List<String> babyIds = new ArrayList<>();
                    for (DocumentSnapshot doc : userBabySnapshots) {
                        UserBaby userBaby = doc.toObject(UserBaby.class);
                        if (userBaby != null) {
                            babyIds.add(userBaby.getBabyId());
                        }
                    }

                    if (babyIds.isEmpty()) {
                        resultLiveData.setValue(new ArrayList<>()); // No babies found
                        return;
                    }

                    // Step 2: Fetch Baby objects
                    FirebaseFirestore.getInstance().collection("Babies")
                            .whereIn(FieldPath.documentId(), babyIds)
                            .get()
                            .addOnSuccessListener(babySnapshots -> {
                                List<Baby> babies = new ArrayList<>();
                                for (DocumentSnapshot babyDoc : babySnapshots) {
                                    Baby baby = babyDoc.toObject(Baby.class);
                                    if (baby != null) {
                                        baby.setIdFs(babyDoc.getId());
                                        babies.add(baby);
                                    }
                                }
                                resultLiveData.setValue(babies);
                            })
                            .addOnFailureListener(e -> resultLiveData.setValue(null));

                })
                .addOnFailureListener(e -> resultLiveData.setValue(null));

        return resultLiveData;
    }

}
