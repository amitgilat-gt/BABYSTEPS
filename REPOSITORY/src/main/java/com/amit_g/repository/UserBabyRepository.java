package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.AllProgress;
import com.amit_g.model.Progress;
import com.amit_g.model.UserBaby;
import com.amit_g.model.UsersBabies;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserBabyRepository extends BaseRepository<UserBaby, UsersBabies> {
    public UserBabyRepository(Application application) {
        super(UserBaby.class, UsersBabies.class, application);
    }

    @Override
    protected Query getQueryForExist(UserBaby entity) {
        return getCollection().whereEqualTo("userId", entity.getUserId());
    }
//    public Task<QuerySnapshot> connectBaby(String idfs, String password) {
//        return getCollection()
//                .whereEqualTo("idFs", idfs)
//                .whereEqualTo("password", password)
//                .get();
//    }
}
