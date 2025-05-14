package math;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class Methods {
    long iter = 0;
    double[] observationalError;
    double intervalLength;
    double phiDerA = 0;
    double phiDerB = 0;
    double simpleIterx0;
    double simpleIterPhi0;

    public Double bisection(Function<Double, Double> f, Double leftBorder, Double rightBorder, Double precision) {
        iter++;
        Double x = (leftBorder + rightBorder) / 2;
        if (Math.abs(f.apply(x)) < precision) {
            intervalLength = Math.abs(rightBorder - leftBorder);
            return x;
        } else {
            if ((f.apply(x) > 0 && f.apply(leftBorder) < 0) || (f.apply(x) < 0 && f.apply(leftBorder) > 0))
                rightBorder = x;
            else
                leftBorder = x;
            return bisection(f, leftBorder, rightBorder, precision);
        }
    }


    public Double secant(Function<Double, Double> f, Double prev, Double prevOfPrev, Double precision) {
        iter++;
        Double x = prev - f.apply(prev) * (prev - prevOfPrev) / (f.apply(prev) - f.apply(prevOfPrev));
        if (Math.abs(f.apply(x)) < precision)
            return x;
        else return secant(f, x, prev, precision);
    }


    public Double simpleIteration(Function<Double, Double> f, Double leftBorder, Double rightBorder, Double precision) {
        Function<Double, Double> derivativeF = derivative(f);

        Double lambda = Math.signum(derivativeF.apply(leftBorder)) * -1 / (Stream.iterate(leftBorder, i -> i + 0.001).limit(1000).filter(e -> e <= rightBorder).map(derivativeF).map(Math::abs).max(Comparator.naturalOrder())).get();
        Function<Double, Double> phi = x -> x + lambda * f.apply(x);

        Function<Double, Double> derivativePhi = x -> 1 + lambda * derivativeF.apply(x);

        phiDerA = derivativePhi.apply(leftBorder);
        phiDerB = derivativePhi.apply(rightBorder);


        if (Math.abs(derivativePhi.apply(leftBorder)) > 1 || Math.abs(derivativePhi.apply(rightBorder)) > 1) {
            return null;
        }

        Double x_0;
        if (derivative(derivative(f)).apply(leftBorder) * f.apply(leftBorder) > 0)
            x_0 = leftBorder;
        else
            x_0 = rightBorder;
        Double x = phi.apply(x_0);
        simpleIterx0 = x_0;
        simpleIterPhi0 = x;
        iter++;
        while (Math.abs(f.apply(x)) > precision) {
            iter++;
            x = phi.apply(x);
        }
        return x;
    }

    public double[] newton(Function<double[], Double> f1, Function<double[], Double> f2, double[] solution, double precision) {
        iter++;
        double[][] matrix = {{SRVderivative(f1, 0).apply(solution), SRVderivative(f1, 1).apply(solution)}, {SRVderivative(f2, 0).apply(solution), SRVderivative(f2, 1).apply(solution)}};
        double[] bs = {-f1.apply(solution), -f2.apply(solution)};
        double[] newSolution = MatrixFunctions.solveLinearSystem(matrix, bs, precision);
        observationalError = new double[]{Math.abs(newSolution[0] - solution[0]), Math.abs(newSolution[1] - solution[1])};
        if (Math.abs(newSolution[0] - solution[0]) < precision && Math.abs(newSolution[1] - solution[1]) < precision) {
            return solution;
        } else return newton(f1, f2, newSolution, precision);
    }

    public void reset() {
        iter = 0;
        observationalError = null;
        intervalLength = 0;
        phiDerA = 0;
        phiDerB = 0;
    }

    public double[] getObservationalError() {
        return observationalError;
    }

    public long getIter() {
        return iter;
    }

    public Function<Double, Double> derivative(Function<Double, Double> f) {
        return x -> (f.apply(x + 0.0000001) - f.apply(x)) / (0.0000001);
    }

    public Function<double[], Double> SRVderivative(Function<double[], Double> f, int index) {
        return x -> {
            double[] old = x.clone();
            x[index] += 0.0000001;
            return (f.apply(x) - f.apply(old)) / 0.0000001;
        };
    }

    public double getIntervalLength() {
        return intervalLength;
    }

    public double getPhiDerA() {
        return phiDerA;
    }

    public double getPhiDerB() {
        return phiDerB;
    }

    public double getSimpleIterx0() {
        return simpleIterx0;
    }

    public double getSimpleIterPhi0() {
        return simpleIterPhi0;
    }
}
