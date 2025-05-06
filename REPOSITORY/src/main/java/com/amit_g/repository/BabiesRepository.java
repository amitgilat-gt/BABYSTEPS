package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class BabiesRepository extends BaseRepository<Baby, Babies> {

    public BabiesRepository(Application application) {
        super(Baby.class, Babies.class, application);
    }

    @Override
    protected Query getQueryForExist(Baby entity) {
        return getCollection().whereEqualTo("BabyName",entity.getName());
    }
}
