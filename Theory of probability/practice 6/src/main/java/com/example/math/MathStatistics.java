package com.example.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MathStatistics {
    public static Collection<? extends Number> variationalSeries(Collection<? extends Number> data){
        return data.stream().sorted(Comparator.comparing(Number::doubleValue)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Double min(Collection<? extends Number> data){
        return data.stream().min(Comparator.comparing(Number::doubleValue)).get().doubleValue();
    }

    public static Double max(Collection<? extends Number> data){
        return data.stream().max(Comparator.comparing(Number::doubleValue)).get().doubleValue();
    }

    public static Double range(Collection<? extends Number> data){
        return max(data) - min(data);
    }

    public static Collection<StatisticalPair> statisticalSeries(Collection<? extends Number> data){
        Collection<? extends Number> variationalSeries = variationalSeries(data);
        Collection<StatisticalPair> statisticalSeries = new ArrayList<>();
        data.forEach(i -> {
            StatisticalPair pair = new StatisticalPair(i, variationalSeries.stream().filter(Predicate.isEqual(i)).count());
            statisticalSeries.add(pair);
        });
        return statisticalSeries.stream().distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Double sampleMean(Collection<? extends Number> data){
        AtomicReference<Double> sampleMean = new AtomicReference<>(0.0);
        data.forEach(i -> sampleMean.set(sampleMean.get() + i.doubleValue()));
        return sampleMean.get() / data.size();
    }

    public static Double sampleVariance(Collection<? extends Number> data){
        double sampleMean = sampleMean(data);
        double v = 0;
        for (int i = 0; i < data.size(); i++){
            v += Math.pow(data.stream().toList().get(i).doubleValue() - sampleMean, 2);
        }
        //AtomicReference<Double> sampleVariance = new AtomicReference<>(0.0);
        //data.forEach(i -> sampleVariance.set(sampleVariance.get() + Math.pow((i.doubleValue() - sampleMean), 2)));
        //return sampleVariance.get() / data.size();
        return v;
    }

    public static Double unbiasedSampleVariance(Collection<? extends Number> data){
        return sampleVariance(data) * data.size() / (data.size() - 1);
    }

    public static Double sampleStandardDeviation(Collection<? extends Number> data){
        return Math.pow(sampleVariance(data), 0.5);
    }

    public static Double correctedSampleStandardDeviation(Collection<? extends Number> data){
        return Math.pow(unbiasedSampleVariance(data), 0.5);
    }

    public static Collection<IntervalStatisticalPair> intervalStatisticalSeries(Collection<? extends Number> data){
        double h = range(data) / (1 + 3.322 * Math.log10(data.size()));
        double x = min(data) - h / 2;
        AtomicLong counter = new AtomicLong();
        ArrayList<IntervalStatisticalPair> intervalStatisticalSeries = new ArrayList<>();
        while (x < max(data)){
            Interval interval = new Interval(x, x + h);
            variationalSeries(data).forEach(i -> {
                if (interval.checkIfBelongs(i)) counter.incrementAndGet();
            });
            intervalStatisticalSeries.add(new IntervalStatisticalPair(interval, counter.get()));
            counter.set(0);
            x += h;
        }
        return intervalStatisticalSeries;
    }
}
