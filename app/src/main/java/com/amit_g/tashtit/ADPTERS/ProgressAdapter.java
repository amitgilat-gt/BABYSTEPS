package com.amit_g.tashtit.ADPTERS;

import com.amit_g.model.Progress;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class ProgressAdapter extends GenericAdapter<Progress> {
    public ProgressAdapter(List<Progress> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<Progress> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
