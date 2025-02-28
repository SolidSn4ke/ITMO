package org.example.commands;

import org.example.math.IterativeMethods;
import org.example.parsers.DoubleParser;
import org.example.parsers.IntParser;
import org.example.system.io.InputManager;
import org.example.system.io.OutputManager;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.math.MatrixFunctions.convertToDiagonallyDominantForm;
import static org.example.math.MatrixFunctions.diagonallyDominant;

public class StartCommand implements Command {
    @Override
    public void execute(String[] args) {
        InputManager im = new InputManager();
        OutputManager om = new OutputManager();
        IterativeMethods iterMethods = new IterativeMethods();

        if (args.length > 1 && !im.changeSource(new File(args[1]))) {
            om.print(String.format("Файл %s не найден", args[1]));
            return;
        }

        Integer dim = new IntParser() {
        }.parse(im, "размерность матрицы", false, false, 20);
        Double precision = new DoubleParser() {
        }.parse(im, "точность", false, false);


        double[][] matrix = new double[dim][dim];
        double[] bs = new double[dim];

        for (int i = 0; i < dim; ) {
            om.print(String.format("Введите коэффициенты %d строчки матрицы через пробел", i + 1));
            om.messageInputPrompt();

            String input = im.tryReadLine();
            if (input == null){
                om.print("Достигнут конец файла. Ввод продолжится с клавиатуры");
                continue;
            }
            String[] row = input.split(" +");

            if (row.length >= dim && Arrays.stream(row).allMatch(str -> new DoubleParser() {
            }.check(str, false, true))) {
                for (int j = 0; j < dim; j++) {
                    matrix[i][j] = Double.parseDouble(row[j].replace(",", "."));
                }
                i++;
            } else om.print(String.format("Пожалуйста, введите необходимое количество чисел(%d) через пробел", dim));
        }

        while (true) {
            om.print("Введите коэффициенты свободных членов");
            om.messageInputPrompt();

            String input = im.tryReadLine();
            if (input == null){
                om.print("Достигнут конец файла. Ввод продолжится с клавиатуры");
                continue;
            }
            String[] row = input.split(" +");

            if (row.length >= dim && Arrays.stream(row).allMatch(str -> new DoubleParser() {
            }.check(str, false, true))) {
                for (int j = 0; j < dim; j++) {
                    bs[j] = Double.parseDouble(row[j].replace(",", "."));
                }
                break;
            } else om.print(String.format("Пожалуйста, введите необходимое количество чисел(%d) через пробел", dim));
        }

        if (diagonallyDominant(matrix) || convertToDiagonallyDominantForm(matrix)) {
            om.print("Матрица с диагональным преобладанием:");
            for (double[] row : matrix) {
                om.print(Arrays.toString(row));
            }
            for (AtomicInteger i = new AtomicInteger(0); i.get() < matrix.length; i.incrementAndGet()) {
                bs[i.get()] = bs[i.get()] / matrix[i.get()][i.get()];
                matrix[i.get()] = Arrays.stream(matrix[i.get()]).map(elem -> -elem / matrix[i.get()][i.get()]).toArray();
                matrix[i.get()][i.get()] = 0;
            }
            om.print("Преобразованная матрица:");
            for (double[] row : matrix) {
                om.print(Arrays.toString(row));
            }
            om.print(String.format("Найденное решение: %s", Arrays.toString(iterMethods.gaussSeidel(matrix, bs, precision))));
            om.print(String.format("Количество итераций: %d", iterMethods.getNumberOfIterations()));
            om.print(String.format("Норма преобразованной матрицы: %s", iterMethods.getMatrixNorm()));
            om.print(String.format("Вектор погрешностей: %s", Arrays.toString(iterMethods.getObservationalError())));
        } else {
            System.out.println("Данная матрица не может быть приведена к виду преобладания диагональных элементов");
        }
    }

    @Override
    public String description() {
        return "start (file_path) - нахождение решения СЛАУ. Если задан аргумент file_path, то данные считываются из файла, иначе с клавиатуры";
    }
}
