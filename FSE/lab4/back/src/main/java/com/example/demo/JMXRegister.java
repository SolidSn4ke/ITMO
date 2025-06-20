package com.example.demo;

import com.example.demo.mbeans.OnTargetPercentMBean;
import com.example.demo.mbeans.DotsCounterMBean;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Singleton
@Startup
public class JMXRegister {

    @Inject
    private DotsCounterMBean dotsCounter;

    @Inject
    OnTargetPercentMBean clicksCounter;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName dots = new ObjectName("com.example.demo:type=dotsCounter");
            ObjectName clicks = new ObjectName("com.example.demo:type=onTargetPercent");
            mbs.registerMBean(dotsCounter, dots);
            mbs.registerMBean(clicksCounter, clicks);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register MBean", e);
        }
    }
}
