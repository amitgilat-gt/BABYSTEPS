package com.amit_g.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amit_g.model.Baby;
import com.amit_g.model.UserBaby;
import com.amit_g.model.UsersBabies;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.UserBabyRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserBabyViewModel extends BaseViewModel<UserBaby, UsersBabies> {
    private UserBabyRepository repository;
    public UserBabyViewModel( Application application) {
        super(UserBaby.class, UsersBabies.class, application);
    }

    @Override
    protected BaseRepository<UserBaby, UsersBabies> createRepository(Application application) {
        repository = new UserBabyRepository(application);
        return repository;
    }
    public LiveData<List<Baby>> getBabiesForUserId(String userId) {
        return repository.getBabiesForUserId(userId);
    }

}
