package com.example.demo.mbeans;

public interface DotsCounterMBean {
    int getAllDots();

    int getOnTargetDots();

    int getConsecutiveMisses();

    void registerNewDot(boolean isOnTarget);
}
