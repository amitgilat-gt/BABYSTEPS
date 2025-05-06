package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.BabiesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;

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
}
