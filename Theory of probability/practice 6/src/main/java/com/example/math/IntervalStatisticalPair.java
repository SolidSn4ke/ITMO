package com.example.math;

public class IntervalStatisticalPair extends OrderedPair<Interval, Long>{
    public IntervalStatisticalPair(Interval a, Long b) {
        super(a, b);
    }

    /**
     * @deprecated
     */
    @Override
    public int compareTo(OrderedPair<Interval, Long> o) {
        return 0;
    }
}
