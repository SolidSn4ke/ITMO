package com.example.back.model.dto;

public class CacheStatsDTO {
    private long hits;
    private long misses;

    public CacheStatsDTO(long hits, long misses) {
        this.hits = hits;
        this.misses = misses;
    }

    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public void setMisses(long misses) {
        this.misses = misses;
    }
}
