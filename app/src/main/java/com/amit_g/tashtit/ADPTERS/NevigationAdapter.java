package com.amit_g.tashtit.ADPTERS;

import com.amit_g.model.btnNevigation;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class NevigationAdapter extends GenericAdapter<btnNevigation> {
    public NevigationAdapter(List<btnNevigation> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<btnNevigation> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
