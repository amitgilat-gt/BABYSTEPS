package com.amit_g.repository;

import android.app.Application;

import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersRepository extends BaseRepository<User, Users> {

    public UsersRepository(Application application) {
        super(User.class, Users.class, application);
    }

    // For sign-up: check if user already exists by username
    @Override
    protected Query getQueryForExist(User entity) {
        return getCollection().whereEqualTo("email", entity.getEmail());
    }

//    public Task<DocumentSnapshot> getUserById(String idFs) {
//        return getCollection().document(idFs).get();
//    }
    public Task<QuerySnapshot> getUserById(String idFs) {
        return getCollection().whereEqualTo("idFs", idFs).get();
    }

    // For login: check if username and password match
    public Task<QuerySnapshot> loginUser(String username, String password) {
        return getCollection()
                .whereEqualTo("email", username)
                .whereEqualTo("password", password)
                .get();
    }



}
