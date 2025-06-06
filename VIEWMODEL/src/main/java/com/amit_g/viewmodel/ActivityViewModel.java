package com.amit_g.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.amit_g.model.LastActivities;
import com.amit_g.model.LastActivity;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.ActivitiesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewModel extends BaseViewModel {
    private ActivitiesRepository repository;

    public ActivityViewModel(Application application) {
        super(LastActivity.class, LastActivities.class, application);
    }

    @Override
    protected BaseRepository createRepository(Application application) {
        repository = new ActivitiesRepository(application);
        return repository;
    }

    // Keep this if needed elsewhere
    public LiveData<List<LastActivity>> getActivitiesForBabyId(String babyId) {
        MutableLiveData<List<LastActivity>> liveData = new MutableLiveData<>();
        repository.getActivitiesForBabyId(babyId).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<LastActivity> activityList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        LastActivity lastActivity = doc.toObject(LastActivity.class);
                        if (lastActivity != null) {
                            activityList.add(lastActivity);
                        }
                    }
                    liveData.setValue(activityList);
                })
                .addOnFailureListener(e -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<LastActivities> listenToActivitiesForBabyId(String babyId) {
        return repository.getAll(null, null, repository.getActivitiesForBabyId(babyId));
    }


}