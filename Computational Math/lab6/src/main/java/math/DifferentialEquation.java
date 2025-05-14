package math;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class DifferentialEquation {
    public int eulerRunge = 0;
    public int rungeRunge = 0;
    public double adamsCheck = 0.0;
    public String eulerRungeResults = "";
    public String rungeRungeResults = "";


    public enum MonoStepType {
        EULER,
        RUNGE
    }

    public enum MultiStepType {
        ADAMS
    }

    Double[] euler(Function<Double[], Double> f, Double x, Double xN, Double y, Double h) {
        Double[] res = new Double[(int) ((xN - x) / h) + 1];
        IntStream.range(0, res.length).forEach(i -> {
            res[i] = i > 0 ? res[i - 1] + h * f.apply(new Double[]{x + h * (i - 1), res[i - 1]}) : y;
        });
        return res;
    }

    Double[] runge(Function<Double[], Double> f, Double x, Double xN, Double y, Double h) {
        Double[] res = new Double[(int) ((xN - x) / h) + 1];
        IntStream.range(0, res.length).forEach(i -> {
            Double k1 = i > 0 ? h * f.apply(new Double[]{x + h * (i - 1), res[i - 1]}) : 0;
            Double k2 = i > 0 ? h * f.apply(new Double[]{x + h * (i - 1) + h / 2, res[i - 1] + k1 / 2}) : 0;
            Double k3 = i > 0 ? h * f.apply(new Double[]{x + h * (i - 1) + h / 2, res[i - 1] + k2 / 2}) : 0;
            Double k4 = i > 0 ? h * f.apply(new Double[]{x + h * (i - 1) + h, res[i - 1] + k3}) : 0;
            res[i] = i > 0 ? res[i - 1] + (double) 1 / 6 * (k1 + 2 * k2 + 2 * k3 + k4) : y;
        });
        return res;
    }

    public double[] monoStep(MonoStepType t, Function<Double[], Double> f, Double x0, Double xN, Double y0, Double h, Double precision) {
        Double[] res = null;
        Double[] res_half_step = null;
        Integer rungeRule = 1;
        Integer accuracy = null;
        switch (t) {
            case EULER -> {
                res = euler(f, x0, xN, y0, h);
                res_half_step = euler(f, x0, xN, y0, h / 2);
                eulerRungeResults = String.format("h: %f, y(%f) = %f, h/2: %f, y(%f) = %f\n", h / rungeRule, xN, res[res.length - 1], h / rungeRule / 2, xN, res_half_step[res_half_step.length - 1]);
                accuracy = 1;
            }
            case RUNGE -> {
                res = runge(f, x0, xN, y0, h);
                res_half_step = runge(f, x0, xN, y0, h / 2);
                rungeRungeResults = String.format("h: %f, y(%f) = %f, h/2: %f, y(%f) = %f\n", h / rungeRule, xN, res[res.length - 1], h / rungeRule / 2, xN, res_half_step[res_half_step.length - 1]);

                accuracy = 15;
            }
        }
        while (!check(res, res_half_step, accuracy, precision)) {
            rungeRule *= 2;
            res = res_half_step;
            switch (t) {
                case EULER -> {
                    res_half_step = euler(f, x0, xN, y0, h / 2 / rungeRule);
                    eulerRungeResults += String.format("h: %f, y(%f) = %f, h/2: %f, y(%f) = %f\n", h / rungeRule, xN, res[res.length - 1], h / rungeRule / 2, xN, res_half_step[res_half_step.length - 1]);
                }
                case RUNGE -> {
                    rungeRungeResults += String.format("h: %f, y(%f) = %f, h/2: %f, y(%f) = %f\n", h / rungeRule, xN, res[res.length - 1], h / rungeRule / 2, xN, res_half_step[res_half_step.length - 1]);
                    res_half_step = runge(f, x0, xN, y0, h / 2 / rungeRule);
                }
            }
        }
        switch (t) {
            case EULER -> eulerRunge = rungeRule;
            case RUNGE -> rungeRunge = rungeRule;
        }
        Integer finalRungeRule = rungeRule;
        Double[] finalRes = res;
        return IntStream.range(0, res.length).filter(i -> i % finalRungeRule == 0).mapToDouble(i -> finalRes[i]).toArray();
    }

    boolean check(Double[] a, Double[] b, Integer accuracy, Double precision) {
        //System.out.println(IntStream.range(0, a.length).mapToDouble(i -> abs(a[i] - b[i * 2])).max().getAsDouble() / accuracy);
        return IntStream.range(0, a.length).mapToDouble(i -> abs(a[i] - b[i * 2])).max().getAsDouble() / accuracy < precision;
    }


    Double[] adams(Function<Double[], Double> f, Double x, Double xN, Double y, Double h) {
        Double[] res = runge(f, x, xN, y, h);
        if (res.length < 4) return null;
        AtomicReference<Double> f0 = new AtomicReference<>(f.apply(new Double[]{x, res[0]}));
        AtomicReference<Double> f1 = new AtomicReference<>(f.apply(new Double[]{x + h, res[1]}));
        AtomicReference<Double> f2 = new AtomicReference<>(f.apply(new Double[]{x + 2 * h, res[2]}));
        AtomicReference<Double> f3 = new AtomicReference<>(f.apply(new Double[]{x + 3 * h, res[3]}));
        IntStream.range(4, res.length).forEach(i -> {
            res[i] = res[i - 1] + h / 24 * (55 * f3.get() - 59 * f2.get() + 37 * f1.get() - 9 * f0.get());
            f0.set(f1.get());
            f1.set(f2.get());
            f2.set(f3.get());
            f3.set(f.apply(new Double[]{x + i * h, res[i]}));
        });
        return res;
    }

    public double[] multiStep(MultiStepType t, Function<Double[], Double> f, Function<Double, Double> correctAnswer, Double x0, Double xN, Double y0, Double h, Double precision) {
        Double[] res = adams(f, x0, xN, y0, h);
        if (res == null) return null;
        AtomicInteger k = new AtomicInteger(1);
        while (!check(correctAnswer, x0, h / k.get(), res, precision)) {
            k.set(k.get() * 2);
            res = adams(f, x0, xN, y0, h / k.get());
        }
        Double[] finalRes = res;
        return IntStream.range(0, res.length).filter(i -> i % k.get() == 0).mapToDouble(i -> finalRes[i]).toArray();
    }

    boolean check(Function<Double, Double> f, Double x0, Double h, Double[] ys, Double precision) {
        adamsCheck = IntStream.range(0, ys.length).mapToDouble(i -> Math.abs(f.apply(x0 + i * h) - ys[i])).max().getAsDouble();
        return IntStream.range(0, ys.length).mapToDouble(i -> Math.abs(f.apply(x0 + i * h) - ys[i])).max().getAsDouble() < precision;
    }
}
