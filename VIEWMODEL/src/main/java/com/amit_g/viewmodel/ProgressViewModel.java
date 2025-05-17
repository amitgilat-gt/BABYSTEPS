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

    public LiveData<List<Progress>> getProgressForBabyId(String babyId) {
        MutableLiveData<List<Progress>> liveData = new MutableLiveData<>();
        repository.getQueryForBabyId(babyId).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Progress> progressList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Progress progress = doc.toObject(Progress.class);
                        if (progress != null) {
                            progressList.add(progress);
                        }
                    }
                    liveData.setValue(progressList);
                })
                .addOnFailureListener(e -> liveData.setValue(null));
        return liveData;
    }

}
