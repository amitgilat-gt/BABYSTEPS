package com.amit_g.repository;

import android.app.Application;
import android.content.Context;

import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class UsersRepository extends BaseRepository<User, Users> {

    public UsersRepository(Application application) {
        super(User.class, Users.class, application);
    }

    @Override
    protected Query getQueryForExist(User entity) {
        return getCollection().whereEqualTo("username",entity.getUserName());
    }
}
