package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.UserBaby;
import com.amit_g.model.UsersBabies;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.UserBabyRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;

public class UserBabyViewModel extends BaseViewModel<UserBaby, UsersBabies> {
    public UserBabyViewModel( Application application) {
        super(UserBaby.class, UsersBabies.class, application);
    }

    @Override
    protected BaseRepository<UserBaby, UsersBabies> createRepository(Application application) {
        return new UserBabyRepository(application);
    }
}
