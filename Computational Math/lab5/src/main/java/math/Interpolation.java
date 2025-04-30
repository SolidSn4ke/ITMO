package math;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Interpolation {
    public String usedValuesFromTable = "";
    public Function<Double, Double> lagrange(double[] xs, double[] ys) {
        return x -> {
            double res = 0;
            for (int i = 0; i < ys.length; i++) {
                double li = 1;
                for (int j = 0; j < xs.length; j++) {
                    if (i == j) continue;
                    li *= (x - xs[j]) / (xs[i] - xs[j]);
                }
                res += ys[i] * li;
            }
            return res;
        };
    }

    public double[][] newtonTable(double[] xs, double[] ys) {
        double[][] table = new double[xs.length + 1][ys.length];
        table[0] = xs;
        table[1] = ys;
        for (int i = 2; i < ys.length + 1; i++) {
            for (int j = 0; j < ys.length + 1 - i; j++) {
                if (checkStep(xs)) {
                    table[i][j] = table[i - 1][j + 1] - table[i - 1][j];
                } else
                    table[i][j] = (table[i - 1][j + 1] - table[i - 1][j]) / (table[0][j + i - 1] - table[0][j]);
            }
        }
        return table;
    }

    public Function<Double, Double> newton(double[] xs, double[] ys) {
        return x -> {
            double res = ys[0];
            double[][] table = newtonTable(xs, ys);
            for (int i = 1; i < ys.length; i++) {
                double fi = 1;
                for (int j = 0; j < i; j++) {
                    fi *= (x - xs[j]);
                    if (checkStep(xs))
                        fi = fi / (xs[1] - xs[0]) / (j + 1);
                }
                res += table[i + 1][0] * fi;
            }
            return res;
        };
    }

    public Function<Double, Double> newtonSameStep(double[] xs, double[] ys) {
        usedValuesFromTable = "";
        return x -> {
            double res = 0;
            double t;
            double[][] table = newtonTable(xs, ys);
            if (x > (xs[xs.length - 1] + xs[0]) / 2) {
                t = calculateT(xs, x);
                res += ys[ys.length - 1];
                for (int i = 2; i <= ys.length; i++) {
                    double fi = 1;
                    for (int j = 0; j < i - 1; j++) {
                        fi *= (t + j) / (j + 1);
                    }
                    res += table[i][ys.length - i] * fi;
                    usedValuesFromTable += table[i][ys.length - i] + " ";
                }
            } else {
                int indexOfClosestX = 0;
                while (x > xs[indexOfClosestX])
                    indexOfClosestX++;
                indexOfClosestX--;
                if (indexOfClosestX < 0){
                    indexOfClosestX = 0;
                }
                res += ys[indexOfClosestX];
                t = calculateT(xs, x);
                for (int i = 2; i <= ys.length - indexOfClosestX; i++) {
                    double fi = 1;
                    for (int j = 0; j < i - 1; j++) {
                        fi *= (t - j) / (j + 1);
                    }
                    res += table[i][indexOfClosestX] * fi;
                    usedValuesFromTable += table[i][indexOfClosestX] + " ";
                }
            }
            return res;
        };
    }

    public Function<Double, Double> stirling(double[] xs, double[] ys) {
        return x -> {
            int middle = (ys.length - 1) / 2;
            double res = ys[middle];
            double[][] table = newtonTable(xs, ys);
            double t = (x - xs[middle]) / (xs[1] - xs[0]);
            double k = t * t;
            for (int i = 2; i <= ys.length; i++) {
                double fi = 1;
                for (int j = 0; j < i - 1 - (i % 2); j++) {
                    fi /= (j + 1);
                }
                if (i % 2 == 0) {
                    fi /= t;
                    res += k * fi * (table[i][middle] + table[i][middle - 1]) / 2;
                    middle--;
                } else {
                    fi /= i - 1;
                    res += k * fi * table[i][middle];
                    k *= (t * t - Math.pow((double) (i - 1) / 2, 2));
                }
            }
            return res;
        };
    }

    public Function<Double, Double> bessel(double[] xs, double[] ys) {
        return x -> {
            int middle = ys.length / 2 - 1;
            double res = 0;
            double[][] table = newtonTable(xs, ys);
            double t = (x - xs[middle]) / (xs[1] - xs[0]);
            for (int i = 1; i <= ys.length; i += 2) {
                double fi = 1;
                for (int j = 0; j < i - 1; j++) {
                    fi *= (t + ((double) (j / 2) + (j % 2)) * Math.pow(-1, j)) / (j + 1);
                }
                res += fi * (table[i][middle] + table[i][middle + 1]) / 2 + (t - 0.5) / i * fi * table[i + 1][middle];
                middle--;
            }
            return res;
        };
    }

    public double calculateT(double[] xs, double x) {
        if (x > (xs[xs.length - 1] - xs[0]) / 2) {
            return (x - xs[xs.length - 1]) / (xs[1] - xs[0]);
        } else {
            int indexOfClosestX = 0;
            while (x > xs[indexOfClosestX])
                indexOfClosestX++;
            return (x - xs[indexOfClosestX]) / (xs[1] - xs[0]);
        }
    }

    public double maxFunctionValue(double[] xs, double[] ys) {
        return Stream.iterate(xs[0], x -> x + (xs[xs.length - 1] - xs[0]) / 10000).limit(10000).filter(x -> x <= xs[xs.length - 1]).map(lagrange(xs, ys)).max(Comparator.naturalOrder()).get();
    }

    public double minFunctionValue(double[] xs, double[] ys) {
        return Stream.iterate(xs[0], x -> x + (xs[xs.length - 1] - xs[0]) / 10000).limit(10000).filter(x -> x <= xs[xs.length - 1]).map(lagrange(xs, ys)).min(Comparator.naturalOrder()).get();
    }

    public boolean checkStep(double[] xs) {
        return IntStream.range(0, xs.length - 2).allMatch(i -> Math.abs((xs[i + 1] - xs[i]) - (xs[i + 2] - xs[i + 1])) <= 0.0000001);
    }

    public boolean checkStirling(double[] xs, double x) {
        return checkStep(xs) && xs.length % 2 == 1 && Math.abs((x - xs[(xs.length - 1) / 2]) / (xs[1] - xs[0])) <= 0.25;
    }

    public boolean checkBessel(double[] xs, double x) {
        return checkStep(xs) && xs.length % 2 == 0 && Math.abs((x - xs[(xs.length - 1) / 2]) / (xs[1] - xs[0])) <= 0.75 && Math.abs((x - xs[(xs.length - 1) / 2]) / (xs[1] - xs[0])) >= 0.25;
    }
}
