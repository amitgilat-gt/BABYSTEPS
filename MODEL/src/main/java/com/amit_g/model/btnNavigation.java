package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.io.Serializable;

public class btnNavigation extends BaseEntity implements Serializable {
    private String label;
    private Class<?> targetActivity;


    public btnNavigation() {
    }

    public btnNavigation(String label, Class<?> targetActivity) {
        this.label = label;
        this.targetActivity = targetActivity;
    }

    public String getLabel() {
        return label;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }
}
