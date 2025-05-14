package math;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Approximation {
    public enum ApproximationType {
        LINEAR,
        SQUARE,
        CUBE,
        POWER,
        EXP,
        LOG;

        @Override
        public String toString() {
            String s = null;
            switch (this) {
                case LINEAR -> s = "Линейная";
                case SQUARE -> s = "Квадратичная";
                case CUBE -> s = "Кубическая";
                case POWER -> s = "Степенная";
                case EXP -> s = "Экспоненциальная";
                case LOG -> s = "Логарифмическая";
            }
            return s;
        }
    }

    public double[] linearApproximation(double[] xs, double[] ys, double precision) {
        double[][] matrix = {{Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).sum()},
                {Arrays.stream(xs).sum(), xs.length}};
        double[] bs = {IntStream.range(0, xs.length).mapToDouble(i -> xs[i] * ys[i]).sum(), Arrays.stream(ys).sum()};
        return MatrixFunctions.solveLinearSystem(matrix, bs, precision);
    }

    public double[] squareApproximation(double[] xs, double[] ys, double precision) {
        double[][] matrix = {{xs.length, Arrays.stream(xs).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum()},
                {Arrays.stream(xs).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum()},
                {Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 4)).sum()}};
        double[] bs = {Arrays.stream(ys).sum(), IntStream.range(0, xs.length).mapToDouble(i -> xs[i] * ys[i]).sum(), IntStream.range(0, xs.length).mapToDouble(i -> Math.pow(xs[i], 2) * ys[i]).sum()};
        return MatrixFunctions.solveLinearSystem(matrix, bs, precision);
    }

    public double[] cubeApproximation(double[] xs, double[] ys, double precision) {
        double[][] matrix = {{xs.length, Arrays.stream(xs).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum()},
                {Arrays.stream(xs).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 4)).sum()},
                {Arrays.stream(xs).map(x -> Math.pow(x, 2)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 4)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 5)).sum()},
                {Arrays.stream(xs).map(x -> Math.pow(x, 3)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 4)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 5)).sum(), Arrays.stream(xs).map(x -> Math.pow(x, 6)).sum()}};
        double[] bs = {Arrays.stream(ys).sum(), IntStream.range(0, xs.length).mapToDouble(i -> xs[i] * ys[i]).sum(), IntStream.range(0, xs.length).mapToDouble(i -> Math.pow(xs[i], 2) * ys[i]).sum(), IntStream.range(0, xs.length).mapToDouble(i -> Math.pow(xs[i], 3) * ys[i]).sum()};
        return MatrixFunctions.solveLinearSystem(matrix, bs, precision);
    }

    public double[] powerApproximation(double[] xs, double[] ys, double precision) {
        double[] res = linearApproximation(Arrays.stream(xs).map(Math::log).toArray(), Arrays.stream(ys).map(Math::log).toArray(), precision);
        res[1] = Math.exp(res[1]);
        return res;
    }

    public double[] exponentialApproximation(double[] xs, double[] ys, double precision) {
        double[] res = linearApproximation(xs, Arrays.stream(ys).map(Math::log).toArray(), precision);
        res[1] = Math.exp(res[1]);
        return res;
    }

    public double[] logApproximation(double[] xs, double[] ys, double precision) {
        return linearApproximation(Arrays.stream(xs).map(Math::log).toArray(), ys, precision);
    }

    public double correlation(double[] xs, double[] ys) {
        return (IntStream.range(0, xs.length).mapToDouble(i -> (xs[i] - Arrays.stream(xs).sum() / xs.length) * (ys[i] - Arrays.stream(ys).sum() / ys.length)).sum()) / (Math.sqrt(Arrays.stream(xs).map(x -> Math.pow(x - Arrays.stream(xs).sum() / xs.length, 2)).sum() * Arrays.stream(ys).map(y -> Math.pow(y - Arrays.stream(ys).sum() / ys.length, 2)).sum()));
    }

    public double standardDeviation(Function<Double, Double> f, double[] xs, double[] ys) {
        return IntStream.range(0, xs.length).mapToDouble(i -> Math.pow(f.apply(xs[i]) - ys[i], 2)).sum();
    }
}
