package math;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class IterativeMethods {
    private long numberOfIterations = 0;
    private double matrixNorm = 0;
    private double[] observationalError;


    public double[] gaussSeidel(double[][] matrix, double[] bs, Double precision) {
        reset();
        //matrixNorm = matrixNorm(matrix);

        int dim = matrix.length;
        double[] xs = bs;

        boolean coverage = false;
        while (!coverage) {
            numberOfIterations++;
            double[] newXS = new double[dim];
            for (int i = 0; i < dim; i++) {
                double s1 = 0.0;
                double s2 = 0.0;
                for (int j = 0; j < dim; j++) {
                    if (j <= i - 1) {
                        s1 += matrix[i][j] * newXS[j];
                    } else if (j >= i + 1) {
                        s2 += matrix[i][j] * xs[j];
                    }
                }
                newXS[i] = (bs[i] + s1 + s2);
            }
            coverage = checkCoverage(newXS, xs, precision);
            calculateObservationalError(newXS, xs);
            xs = newXS;
            //System.out.println(Arrays.toString(xs));
        }
        return xs;
    }


    private static boolean checkCoverage(double[] l1, double[] l2, double precision) {
        boolean result = true;
        for (int i = 0; i < min(l1.length, l2.length); i++) {
            if (abs(l1[i] - l2[i]) > precision) {
                result = false;
                break;
            }
        }
        return result;
    }


    private void calculateObservationalError(double[] newXS, final double[] xs) {
        AtomicInteger i = new AtomicInteger(0);
        observationalError = Arrays.stream(newXS).map(elem -> abs(elem - xs[i.getAndIncrement()])).toArray();
    }


    private void reset() {
        numberOfIterations = 0;
        matrixNorm = 0;
        observationalError = null;
    }

    public long getNumberOfIterations() {
        return numberOfIterations;
    }

    public double getMatrixNorm() {
        return matrixNorm;
    }

    public double[] getObservationalError() {
        return observationalError;
    }
}