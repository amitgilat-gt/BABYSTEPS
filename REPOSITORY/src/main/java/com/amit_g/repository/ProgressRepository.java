package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.AllProgress;
import com.amit_g.model.Progress;
import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class ProgressRepository extends BaseRepository<Progress, AllProgress> {

    public ProgressRepository(Application application) {
        super(Progress.class, AllProgress.class, application);
    }

    @Override
    protected Query getQueryForExist(Progress entity) {
        return getCollection().whereEqualTo("babyId", entity.getBabyId());
    }
}
