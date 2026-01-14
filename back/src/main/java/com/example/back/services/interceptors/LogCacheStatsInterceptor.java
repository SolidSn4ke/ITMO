package com.example.back.services.interceptors;

import com.example.back.config.LogCacheStats;
import com.example.back.model.dto.CacheStatsDTO;
import com.example.back.services.CacheStatisticsService;
import com.example.back.util.listeners.SqlQueryListener;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@LogCacheStats
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LogCacheStatsInterceptor {
    @Inject
    CacheStatisticsService css;

    @AroundInvoke
    public Object logStats(InvocationContext context) throws Exception {
        SqlQueryListener.reset();

        Object res = context.proceed();

        if (SqlQueryListener.wasSqlExecuted()) {
            css.miss();
        } else {
            css.hit();
        }
        CacheStatsDTO stats = css.getStatistics();
        System.out.printf("L2 Cache:\n\thits = %d\n\tmisses = %d\n", stats.getHits(), stats.getMisses());

        return res;
    }
}
