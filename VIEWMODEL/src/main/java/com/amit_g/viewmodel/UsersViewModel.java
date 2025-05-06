package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.UsersRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersViewModel extends BaseViewModel<User, Users> {
    private UsersRepository repository;

    public UsersViewModel( Application application) {
        super(User.class, Users.class, application);
    }

    @Override
    protected BaseRepository<User, Users> createRepository(Application application) {
        repository = new UsersRepository(application);
        return repository;
    }
    public Task<QuerySnapshot> loginUser(String username, String password) {
        return repository.loginUser(username, password);
    }


}
