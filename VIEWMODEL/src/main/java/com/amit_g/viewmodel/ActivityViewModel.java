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
    private final MutableLiveData<String> selectedBabyId = new MutableLiveData<>();
    private final LiveData<LastActivity> latestActivity;

    public ActivityViewModel(Application application) {
        super(LastActivity.class, LastActivities.class, application);
        repository = new ActivitiesRepository(application);

        latestActivity = Transformations.switchMap(selectedBabyId, babyId -> {
            if (babyId == null) {
                return new MutableLiveData<>(null);
            }

            MutableLiveData<LastActivity> liveData = new MutableLiveData<>();
            repository.getLatestActivityForBabyId(babyId).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                            LastActivity activity = doc.toObject(LastActivity.class);
                            liveData.setValue(activity);
                        } else {
                            liveData.setValue(null);
                        }
                    })
                    .addOnFailureListener(e -> liveData.setValue(null));
            return liveData;
        });
    }

    @Override
    protected BaseRepository createRepository(Application application) {
        return repository;
    }

    public void setSelectedBabyId(String babyId) {
        selectedBabyId.setValue(babyId);
    }

    public LiveData<LastActivity> getLatestActivity() {
        return latestActivity;
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
}