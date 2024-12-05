package com.example.math;

public class StatisticalPair extends OrderedPair<Number, Long>{

    public StatisticalPair(Number a, Long b){
        super(a, b);
    }

    @Override
    public int compareTo(OrderedPair o) {
        return Double.compare(this.getA().doubleValue(), ((Number) o.getA()).doubleValue());
    }
}
