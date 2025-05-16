package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class BabiesRepository extends BaseRepository<Baby, Babies> {

    public BabiesRepository(Application application) {
        super(Baby.class, Babies.class, application);
    }

    @Override
    protected Query getQueryForExist(Baby entity) {
        return getCollection().whereEqualTo("BabyName",entity.getName());
    }

    public Task<QuerySnapshot> connectBaby(String id, String password) {
        return getCollection()
                .whereEqualTo("idB", id)
                .whereEqualTo("password", password)
                .get();
    }
    public Task<DocumentSnapshot> getBabyById(String babyId) {
        return getCollection().document(babyId).get();
    }
}
