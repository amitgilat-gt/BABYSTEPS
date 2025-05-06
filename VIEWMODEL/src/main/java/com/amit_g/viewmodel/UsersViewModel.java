package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.User;
import com.amit_g.model.Users;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.UsersRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;

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
}
