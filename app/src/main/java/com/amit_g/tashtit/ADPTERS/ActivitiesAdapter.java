package com.amit_g.tashtit.ADPTERS;

import com.amit_g.model.LastActivity;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class ActivitiesAdapter extends GenericAdapter<LastActivity> {
    public ActivitiesAdapter(List<LastActivity> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<LastActivity> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
