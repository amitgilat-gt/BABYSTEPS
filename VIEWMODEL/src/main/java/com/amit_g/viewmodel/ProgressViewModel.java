package com.amit_g.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amit_g.model.AllProgress;
import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.model.Progress;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.BabiesRepository;
import com.amit_g.repository.ProgressRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ProgressViewModel extends BaseViewModel<Progress, AllProgress> {
    private ProgressRepository repository;

    public ProgressViewModel( Application application) {
        super(Progress.class, AllProgress.class, application);
    }
    @Override
    protected BaseRepository<Progress, AllProgress> createRepository(Application application) {
        repository = new ProgressRepository(application);
        return repository;
    }

    public LiveData<AllProgress> listenToProgressForBabyId(String babyId) {
        return repository.getAll(null, null, repository.getQueryForBabyId(babyId));
    }




}
