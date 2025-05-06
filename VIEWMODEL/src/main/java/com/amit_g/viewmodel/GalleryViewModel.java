package com.amit_g.viewmodel;

import android.app.Application;

import com.amit_g.model.Babies;
import com.amit_g.model.Baby;
import com.amit_g.model.Galleries;
import com.amit_g.model.Gallery;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.BabiesRepository;
import com.amit_g.repository.GalleriesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;

public class GalleryViewModel extends BaseViewModel<Gallery, Galleries> {
    private GalleriesRepository repository;

    public GalleryViewModel(Application application) {
        super(Gallery.class, Galleries.class, application);
    }

    @Override
    protected BaseRepository<Gallery, Galleries> createRepository(Application application) {
        repository = new GalleriesRepository(application);
        return repository;
    }
}
