package com.example.demo.mbeans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.management.*;
import java.io.Serializable;

@ApplicationScoped
@Named("dotsCounter")
public class DotsCounter implements DotsCounterMBean, Serializable, NotificationBroadcaster {
    int allDots = 0;
    int onTargetDots = 0;
    int consecutiveMisses = 0;
    transient NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();

    @Override
    public int getAllDots() {
        return allDots;
    }

    @Override
    public int getOnTargetDots() {
        return onTargetDots;
    }

    @Override
    public int getConsecutiveMisses() {
        return consecutiveMisses;
    }

    @Override
    public void registerNewDot(boolean isOnTarget) {
        allDots++;
        if (isOnTarget) {
            onTargetDots++;
            consecutiveMisses = 0;
        } else
            consecutiveMisses++;
        if (consecutiveMisses == 4) {
            consecutiveMisses = 0;
            Notification notification = new Notification("four.misses.in.a.row", "DotsCounter", System.currentTimeMillis(), "User missed 4 times in a row!");
            broadcaster.sendNotification(notification);
        }
    }

    @Override
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        broadcaster.addNotificationListener(listener, filter, handback);
    }

    @Override
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        broadcaster.removeNotificationListener(listener);
    }


    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{"four.misses.in.a.row"};
        String name = Notification.class.getName();
        String description = "Occurred when user missed 4 times in a row";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }
}
