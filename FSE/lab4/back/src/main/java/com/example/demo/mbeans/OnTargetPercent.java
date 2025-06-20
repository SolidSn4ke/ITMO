package com.example.demo.mbeans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

@ApplicationScoped
@Named("onTargetPercent")
public class OnTargetPercent implements Serializable, OnTargetPercentMBean {
    double percentage = 0.0;
    int allDots = 0;
    int onTargetDots = 0;

    @Override
    public double getPercent() {
        return percentage;
    }

    @Override
    public void updatePercentage(boolean isOnTarget) {
        allDots++;
        if (isOnTarget)
            onTargetDots++;
        percentage = (double) onTargetDots / allDots * 100;
    }
}
