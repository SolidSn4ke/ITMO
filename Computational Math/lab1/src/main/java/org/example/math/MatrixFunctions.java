package org.example.math;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

public class MatrixFunctions {
    public static boolean diagonallyDominant(double[][] matrix) {
        boolean result = true;
        for (int i = 0; i < matrix.length; i++) {
            if (abs(matrix[i][i]) < (Arrays.stream(matrix[i]).map(Math::abs).sum() - abs(matrix[i][i]))) {
                result = false;
            }
        }
        return result;
    }


    private static boolean swapRows(double[][] matrix, int index1, int index2) {
        if (index1 >= 0 && index2 >= 0 && index1 != index2 && index1 <= matrix.length - 1 && index2 <= matrix.length - 1) {
            double[] buf = matrix[index1];
            matrix[index1] = matrix[index2];
            matrix[index2] = buf;
            return true;
        } else return false;
    }


    private static boolean swapColumns(double[][] matrix, int index1, int index2) {
        if (index1 >= 0 && index2 >= 0 && index1 != index2 && index1 <= matrix[0].length - 1 && index2 <= matrix[0].length - 1) {
            for (int i = 0; i < matrix.length; i++) {
                double buf = matrix[i][index1];
                matrix[i][index1] = matrix[i][index2];
                matrix[i][index2] = buf;
            }
            return true;
        } else return false;
    }


    public static boolean convertToDiagonallyDominantForm(double[][] matrix) {
        boolean possible = true;
        ArrayList<Integer> positions = new ArrayList<>(0);

        for (double[] row : matrix) {
            if (abs(Arrays.stream(row).max().getAsDouble()) < Arrays.stream(row).map(Math::abs).sum() - abs(Arrays.stream(row).max().getAsDouble())) {
                possible = false;
            } else {
                int pos = 0;
                for (double e : row){
                    if (e == Arrays.stream(row).max().getAsDouble()){
                        break;
                    }
                    pos++;
                }
                positions.add(pos);
            }
        }

        if (possible && positions.stream().distinct().count() == matrix.length){
            int current = 0;
            while (current != positions.size() - 1){
                if (positions.get(current) > positions.get(current + 1)){
                    swapColumns(matrix, positions.get(current), positions.get(current + 1));
                    int buf = positions.get(current);
                    positions.set(current, positions.get(current + 1));
                    positions.set(current + 1, buf);
                    current = 0;
                } else current++;
            }
            return true;
        } else return false;
    }


    public static double matrixNorm(double[][] matrix){
        return Arrays.stream(matrix).mapToDouble(row -> Arrays.stream(row).map(Math::abs).sum()).max().getAsDouble();
    }
}
