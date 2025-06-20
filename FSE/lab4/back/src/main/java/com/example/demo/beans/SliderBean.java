package com.example.demo.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;


/**
 * @deprecated
 */
@Named
@RequestScoped
public class SliderBean implements Serializable {
    private int minValue = -4;
    private int maxValue = 4;
    private boolean showLabels = true;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }
}
