package com.example.math;

import java.util.Objects;

public abstract class OrderedPair<T, E> implements Comparable<OrderedPair<T, E>>{
    private T a;
    private E b;

    public OrderedPair(T a, E b){
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return a;
    }

    public void setA(T a) {
        this.a = a;
    }

    public E getB() {
        return b;
    }

    public void setB(E b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedPair<?, ?> that = (OrderedPair<?, ?>) o;

        if (!Objects.equals(a, that.a)) return false;
        return Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderedPair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
