package com.amit_g.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.BabiesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class BabiesViewModel extends BaseViewModel<Baby, Babies> {
    private BabiesRepository repository;

    public BabiesViewModel(Application application) {
        super(Baby.class, Babies.class, application);
    }

    @Override
    protected BaseRepository<Baby,Babies> createRepository(Application application) {
        repository = new BabiesRepository(application);
        return repository;
    }
    public Task<QuerySnapshot> connectBaby(String id, String password) {
        return repository.connectBaby(id, password);
    }
    public Task<DocumentSnapshot> getBabyById(String babyId) {
        return repository.getBabyById(babyId);
    }

}
