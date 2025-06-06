package com.amit_g.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amit_g.model.LastActivities;
import com.amit_g.model.LastActivity;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class ActivitiesRepository extends BaseRepository<LastActivity, LastActivities> {
    public ActivitiesRepository(Application application) {
        super(LastActivity.class, LastActivities.class, application);
    }

    @Override
    protected Query getQueryForExist(LastActivity entity) {
        return getCollection().whereEqualTo("babyId", entity.getBabyId());
    }
    public Query getActivitiesForBabyId(String babyId) {
        return getCollection().whereEqualTo("babyId", babyId);
    }
    public LiveData<LastActivities> listenToActivitiesForBabyId(String babyId) {
        return getAll(null, null, getActivitiesForBabyId(babyId));
    }


}
