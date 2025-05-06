package com.amit_g.repository;

import com.google.firebase.firestore.Query;

import com.amit_g.model.BASE.BaseEntity;
import com.amit_g.repository.BASE.BaseRepository;

public class Xrepository extends BaseRepository {
    @Override
    protected Query getQueryForExist(BaseEntity entity) {
        return null;
    }
}
