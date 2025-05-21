package com.amit_g.tashtit.ADPTERS;

import com.amit_g.model.btnNavigation;
import com.amit_g.tashtit.ADPTERS.BASE.GenericAdapter;

import java.util.List;

public class NevigationAdapter extends GenericAdapter<btnNavigation> {
    public NevigationAdapter(List<btnNavigation> items, int layoutId, InitializeViewHolder initializeViewHolder, BindViewHolder<btnNavigation> bindViewHolder) {
        super(items, layoutId, initializeViewHolder, bindViewHolder);
    }
}
