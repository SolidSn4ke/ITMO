package com.example.back.util.parcers;

public interface Parcer<T1, T2> {
    T2 parse(T1 toParce);
}
