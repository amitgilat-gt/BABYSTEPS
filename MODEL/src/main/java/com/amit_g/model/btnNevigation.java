package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.io.Serializable;

public class btnNevigation extends BaseEntity implements Serializable {
    private String label;
    private Class<?> targetActivity;


    public btnNevigation() {
    }

    public btnNevigation(String label, Class<?> targetActivity) {
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
