package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.firebase.firestore.Query;

public class UsersRepository extends BaseRepository<User, Users> {

    public UsersRepository(Application application) {
        super(User.class, Users.class, application);
    }

    // For sign-up: check if user already exists by username
    @Override
    protected Query getQueryForExist(User entity) {
        return getCollection().whereEqualTo("username", entity.getUserName());
    }

    // For login: check if username and password match
    public Query getQueryForLogin(String username, String password) {
        return getCollection()
                .whereEqualTo("username", username)
                .whereEqualTo("password", password);
    }
}
