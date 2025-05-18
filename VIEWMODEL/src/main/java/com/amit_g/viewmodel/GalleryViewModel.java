package com.amit_g.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amit_g.model.Galleries;
import com.amit_g.model.Gallery;
import com.amit_g.repository.BASE.BaseRepository;
import com.amit_g.repository.GalleriesRepository;
import com.amit_g.viewmodel.BASE.BaseViewModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
    public LiveData<List<Gallery>> getPicturesForBabyId(String babyId) {
        MutableLiveData<List<Gallery>> liveData = new MutableLiveData<>();
        repository.getPicturesForBabyId(babyId).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Gallery> GalleriesList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Gallery gallery = doc.toObject(Gallery.class);
                        if (gallery != null) {
                            GalleriesList.add(gallery);
                        }
                    }
                    liveData.setValue(GalleriesList);
                })
                .addOnFailureListener(e -> liveData.setValue(null));
        return liveData;
    }
}
