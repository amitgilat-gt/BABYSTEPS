package com.amit_g.tashtit.ADPTERS;

import com.amit_g.model.Gallery;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class GalleryAdapter extends GenericAdapter<Gallery> {
    public GalleryAdapter(List<Gallery> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<Gallery> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
