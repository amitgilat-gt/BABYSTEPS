package com.amit_g.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amit_g.model.Galleries;
import com.amit_g.model.Gallery;
import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class GalleriesRepository extends BaseRepository<Gallery, Galleries> {

    public GalleriesRepository(Application application) {
        super(Gallery.class, Galleries.class, application);
    }

    @Override
    protected Query getQueryForExist(Gallery entity) {
        return getCollection().whereEqualTo("picture",entity.getPicture());
    }

    public Query getPicturesForBabyId(String babyId) {
        return getCollection().whereEqualTo("babyId",babyId);
    }
    public LiveData<Galleries> listenToPicturesForBabyId(String babyId) {
        return getAll(null, null, getPicturesForBabyId(babyId));
    }

}
