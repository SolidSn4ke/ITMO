package com.example.back.services;

import java.util.concurrent.atomic.AtomicLong;

import com.example.back.model.dto.CacheStatsDTO;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheStatisticsService {

    private AtomicLong hits = new AtomicLong(0);
    private AtomicLong misses = new AtomicLong(0);

    public long hit() {
        return hits.incrementAndGet();
    }

    public long miss() {
        return misses.incrementAndGet();
    }

    public CacheStatsDTO getStatistics() {
        return new CacheStatsDTO(hits.get(), misses.get());
    }
}
