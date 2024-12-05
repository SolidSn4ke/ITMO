package com.example.math;

public class Interval extends OrderedPair<Double, Double>{
    public Interval(Double a, Double b) {
        super(Math.min(a, b), Math.max(a, b));
    }

    /**
     * @deprecated
     */
    @Override
    public int compareTo(OrderedPair<Double, Double> o) {
        return 0;
    }

    public boolean checkIfBelongs(Number n){
        return this.getA() < n.doubleValue() && this.getB() > n.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("[%f; %f]", this.getA(), this.getB());
    }
}
