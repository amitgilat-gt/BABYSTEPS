package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.LastActivities;
import com.amit_g.model.LastActivity;
import com.amit_g.model.Note;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.ActivitiesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;

public class ActivityViewModel extends BaseViewModel<LastActivity, LastActivities> {
    private ActivitiesRepository repository;

    public ActivityViewModel(Application application) {
        super(LastActivity.class, LastActivities.class, application);
    }

    @Override
    protected BaseRepository<LastActivity, LastActivities> createRepository(Application application) {
        repository = new ActivitiesRepository(application);
        return repository;
    }
}