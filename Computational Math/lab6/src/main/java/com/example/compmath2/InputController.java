package com.example.compmath2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import math.Approximation;
import math.DifferentialEquation;
import math.Interpolation;
import math.Methods;
import validators.DoubleValidator;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InputController implements Initializable {
    ToggleGroup systems;
    ToggleGroup functions;
    ToggleGroup methods;
    ToggleGroup diff;
    DoubleValidator dv = new DoubleValidator();
    Methods m = new Methods();
    @FXML
    VBox approx;
    @FXML
    RadioButton s1;
    @FXML
    Label systemsLabel;
    @FXML
    Label precisionLabel;
    @FXML
    Label rightBorderLabel;
    @FXML
    Label leftBorderLabel;
    @FXML
    VBox functionsGroup;
    @FXML
    Label functionsLabel;
    @FXML
    VBox methodsGroup;
    @FXML
    Label methodsLabel;
    @FXML
    RadioButton f1;
    @FXML
    Button submit;
    @FXML
    ComboBox<String> taskSelector;
    @FXML
    RadioButton bisection;
    @FXML
    RadioButton secant;
    @FXML
    RadioButton simpleIteration;
    @FXML
    TextField leftBorder;
    @FXML
    TextField rightBorder;
    @FXML
    TextField precision;
    @FXML
    Canvas canvas;
    @FXML
    TextArea logs;
    @FXML
    Label errorLabel;
    @FXML
    TextField fileIn;
    @FXML
    TextField fileOut;
    @FXML
    VBox controls;
    @FXML
    VBox systemsGroup;
    @FXML
    Button submitSystem;
    @FXML
    RadioButton s2;
    @FXML
    Label x0Label;
    @FXML
    Label y0Label;
    @FXML
    TextField x0;
    @FXML
    TextField y0;
    @FXML
    RadioButton f2;
    @FXML
    RadioButton f3;
    @FXML
    VBox io;
    @FXML
    TextField xs;
    @FXML
    TextField ys;
    @FXML
    Button submitApprox;
    @FXML
    TextField interpolNum;
    @FXML
    Label interpolNumLabel;
    @FXML
    RadioButton f4;
    @FXML
    Label interpolXLabel;
    @FXML
    TextField interpolX;
    @FXML
    VBox diffGroup;
    @FXML
    RadioButton d1;
    @FXML
    RadioButton d2;
    @FXML
    RadioButton d3;
    @FXML
    Label stepLabel;
    @FXML
    TextField step;
    @FXML
    Label initYLabel;
    @FXML
    TextField initY;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methods = new ToggleGroup();
        bisection.setToggleGroup(methods);
        secant.setToggleGroup(methods);
        simpleIteration.setToggleGroup(methods);

        functions = new ToggleGroup();
        f1.setToggleGroup(functions);
        f2.setToggleGroup(functions);
        f3.setToggleGroup(functions);

        systems = new ToggleGroup();
        s1.setToggleGroup(systems);
        s1.setText("sin(x) + 2 * y = 2\nx + cos(y - 1) = 0,7");
        s2.setToggleGroup(systems);
        s2.setText("sin(x + y) - 1,2 * x\nx^2 + 2 * y^2 = 1");

        diff = new ToggleGroup();
        d1.setToggleGroup(diff);
        d2.setToggleGroup(diff);
        d3.setToggleGroup(diff);

        taskSelector.getItems().addAll("Нелинейные уравнения", "Системы нелинейных уравнений", "Аппроксимация функции", "Интерполяция функции", "Дифференциальные уравнения");
        taskSelector.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            controls.getChildren().removeAll(controls.getChildren());
            switch (newValue) {
                case "Нелинейные уравнения" ->
                        controls.getChildren().addAll(taskSelector, methodsLabel, methodsGroup, functionsLabel, functionsGroup, leftBorderLabel, leftBorder, rightBorderLabel, rightBorder, precisionLabel, precision, io, submit, errorLabel);
                case "Системы нелинейных уравнений" ->
                        controls.getChildren().addAll(taskSelector, systemsLabel, systemsGroup, x0Label, x0, y0Label, y0, precisionLabel, precision, submitSystem, errorLabel);
                case "Аппроксимация функции" -> {
                    controls.getChildren().addAll(taskSelector, io, precisionLabel, precision, approx, errorLabel);
                    submitApprox.setText("Аппроксимировать");
                    submitApprox.setOnAction(e -> onSubmitApproxButtonClick());
                }
                case "Интерполяция функции" -> {
                    f4.setToggleGroup(functions);
                    controls.getChildren().addAll(taskSelector, io, functionsLabel, functionsGroup, f4, leftBorderLabel, leftBorder, rightBorderLabel, rightBorder, interpolNumLabel, interpolNum, interpolXLabel, interpolX, approx, errorLabel);
                    submitApprox.setText("Интерполировать");
                    submitApprox.setOnAction(e -> onSubmitInterpolButtonClick());
                }
                case "Дифференциальные уравнения" -> {
                    controls.getChildren().addAll(taskSelector, diffGroup, initYLabel, initY, leftBorderLabel, leftBorder, rightBorderLabel, rightBorder, stepLabel, step, precisionLabel, precision, submit, errorLabel);
                    submit.setOnAction(e -> onSubmitDiffButtonClick());
                }
            }
        });
        taskSelector.getSelectionModel().selectFirst();
    }


    @FXML
    public void onSubmitButtonClick() {
        m.reset();
        reset();

        Function<Double, Double> f;
        Double left;
        Double right;
        Double p;

        double secondDerValueFA;
        double secondDerValueFB;
        double secantx0 = 0;
        double secantxnext = 0;

        if (!fileIn.getText().equals("")) {
            File filepath = new File(fileIn.getText());
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                br.readLine();
                methods.getToggles().get(Integer.parseInt(br.readLine()) - 1).setSelected(true);
                functions.getToggles().get(Integer.parseInt(br.readLine()) - 1).setSelected(true);
                leftBorder.setText(br.readLine());
                rightBorder.setText(br.readLine());
                precision.setText(br.readLine());
            } catch (IOException e) {
                errorLabel.setText(String.format("Файл %s не найден или имеет неверный формат", fileIn.getText()));
                return;
            } catch (IndexOutOfBoundsException e) {
                errorLabel.setText(String.format("Файл %s имеет неверный формат", fileIn.getText()));
                return;
            }
        }

        Double res;

        if (dv.validate(leftBorder.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)
                && dv.validate(rightBorder.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)
                && dv.validate(precision.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)) {
            left = Double.parseDouble(leftBorder.getText().trim().replace(",", "."));
            right = Double.parseDouble(rightBorder.getText().trim().replace(",", "."));
            p = Double.parseDouble(precision.getText().trim().replace(",", "."));

            if (p <= 0) {
                errorLabel.setText("Точность не может быть отрицательным числом");
                return;
            }

            if (left > right) {
                errorLabel.setText("Левый конец интервала не может быть больше правого");
                return;
            }


            if (functions.getSelectedToggle() != null) {
                switch (((RadioButton) functions.getSelectedToggle()).getId()) {
                    case "f1" -> f = x -> 2 * Math.pow(x, 3) + 3.41 * Math.pow(x, 2) - 23.74 * x + 2.95;
                    case "f2" -> f = x -> Math.sin(Math.exp(x));
                    case "f3" -> f = x -> Math.tan(Math.sin(x) - Math.cos(x));
                    default -> {
                        errorLabel.setText("Выберите функцию, корень которой необходимо вычислить");
                        return;
                    }
                }
            } else {
                errorLabel.setText("Выберите функцию, корень которой необходимо вычислить");
                return;
            }

            secondDerValueFA = m.derivative(m.derivative(f)).apply(left);
            secondDerValueFB = m.derivative(m.derivative(f)).apply(right);

            if (!checkInterval(f, left, right)) {
                if (!fileOut.getText().equals("")) {
                    File file = new File(fileOut.getText());
                    try {
                        if (!file.exists())
                            file.createNewFile();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        bw.write(String.format("На интервале [%f;%f] данная функция не имеет корней или имеет больше одного корня\n", left, right));
                        bw.flush();
                        bw.close();
                        logs.setText(String.format("Результат работы успешно записан в файл %s", file.getName()));
                    } catch (IOException e) {
                        logs.setText(String.format("Произошла ошибка во время записи результата в файл %s", file.getName()));
                    }
                } else {
                    logs.setText(String.format("На интервале [%f;%f] данная функция не имеет корней или имеет больше одного корня\n", left, right));
                }
                drawFunction(canvas.getGraphicsContext2D(), f, left, right, null);
                return;
            }


            if (methods.getSelectedToggle() != null) {
                switch (((RadioButton) methods.getSelectedToggle()).getText()) {
                    case "Метод бисекции" -> res = m.bisection(f, left, right, p);
                    case "Метод секущих" -> {
                        if (m.derivative(m.derivative(f)).apply(right) * f.apply(right) > 0) {
                            res = m.secant(f, right - (right - left) / 100, right, p);
                            secantx0 = right;
                            secantxnext = right - (right - left) / 100;
                        } else {
                            res = m.secant(f, left + (right - left) / 100, left, p);
                            secantx0 = left;
                            secantxnext = left + (right - left) / 100;
                        }
                    }
                    case "Метод простой итерации" -> res = m.simpleIteration(f, left, right, p);
                    default -> {
                        errorLabel.setText("Выберите метод, которым будет вычислен корень");
                        return;
                    }
                }
                if (!fileOut.getText().equals("")) {
                    File file = new File(fileOut.getText());
                    try {
                        if (!file.exists())
                            file.createNewFile();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        if (res == null) {
                            bw.write(String.format("Для данной функции на интервале [%f;%f] не выполнено достаточное условие сходимости метода простой итерации", left, right));
                        } else {
                            bw.write(String.format("Найденное решение: %f\nf(%f) = %f\nКоличество итераций: %d", res, res, f.apply(res), m.getIter()));
                        }
                        bw.flush();
                        bw.close();
                        logs.setText(String.format("Результат работы успешно записан в файл %s", file.getName()));
                    } catch (IOException e) {
                        logs.setText(String.format("Произошла ошибка во время записи результата в файл %s", file.getName()));
                    }
                } else {
                    if (res == null) {
                        logs.setText(String.format("Для данной функции на интервале [%f;%f] не выполнено достаточное условие сходимости метода простой итерации\nPhi'(%f) = %f\nPhi'(%f) = %f", left, right, left, m.getPhiDerA(), right, m.getPhiDerB()));
                    } else {
                        if (((RadioButton) methods.getSelectedToggle()).getText().equals("Метод бисекции")) {
                            logs.setText(String.format("Найденное решение: %f\nf(%f) = %f\nКоличество итераций: %d\nДлина интервала: %f", res, res, f.apply(res), m.getIter(), m.getIntervalLength()));
                        } else if (((RadioButton) methods.getSelectedToggle()).getText().equals("Метод секущих"))
                            logs.setText(String.format("Найденное решение: %f\nf(%f) = %f\nКоличество итераций: %d\nf(%f) = %f\nf(%f) = %f\nf''(%f) = %f\n f''(%f) = %f\nx_0 = %f\nx_1 = %f", res, res, f.apply(res), m.getIter(), left, f.apply(left), right, f.apply(right), left, secondDerValueFA, right, secondDerValueFB, secantx0, secantxnext));
                        else
                            logs.setText(String.format("Найденное решение: %f\nf(%f) = %f\nКоличество итераций: %d\nPhi'(%f) = %f\nPhi'(%f) = %f\nx_0: %f\nPhi(%f) = %f", res, res, f.apply(res), m.getIter(), left, m.getPhiDerA(), right, m.getPhiDerB(), m.getSimpleIterx0(), m.getSimpleIterx0(), m.getSimpleIterPhi0()));
                    }
                }
                drawFunction(canvas.getGraphicsContext2D(), f, left, right, res);
            } else
                errorLabel.setText("Выберите метод, которым будет вычислен корень");
        } else {
            errorLabel.setText("Границы интервалов и точность должны быть числами");
        }
    }

    @FXML
    void onSubmitSystemButtonClick() {
        m.reset();
        errorLabel.setText("");
        Function<double[], Double> f1 = null;
        Function<double[], Double> f2 = null;
        double[] res;
        double p;
        double startX;
        double startY;
        if (systems.getSelectedToggle() != null) {
            if (dv.validate(precision.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE) &&
                    dv.validate(x0.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE) &&
                    dv.validate(y0.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)) {
                p = Double.parseDouble(precision.getText().replace(",", "."));
                startX = Double.parseDouble(x0.getText().replace(",", "."));
                startY = Double.parseDouble(y0.getText().replace(",", "."));
                if (p <= 0) {
                    errorLabel.setText("Точность не может быть отрицательным числом");
                }
                switch (((RadioButton) systems.getSelectedToggle()).getId()) {
                    case "s1" -> {
                        f1 = x -> Math.sin(x[0]) + 2 * x[1] - 2;
                        f2 = x -> x[0] + Math.cos(x[1] - 1) - 0.7;
                    }
                    case "s2" -> {
                        f1 = x -> Math.sin(x[0] + x[1]) - 1.2 * x[0] - 0.2;
                        f2 = x -> Math.pow(x[0], 2) + 2 * Math.pow(x[1], 2) - 1;
                    }
                }
                res = m.newton(f1, f2, new double[]{startX, startY}, p);
                logs.setText(String.format("Найденное решение системы: %s\nКоличество итераций: %d\nВектор погрешностей: %s\nf(%s) = %f\nf(%s) = %f", Arrays.toString(res), m.getIter(), Arrays.toString(m.getObservationalError()), Arrays.toString(res), f1.apply(res), Arrays.toString(res), f2.apply(res)));
                drawSystem(f1, f2, res);
            } else {
                errorLabel.setText("Точность и начальное приближение должны быть числами");
            }
        } else
            errorLabel.setText("Выберите систему, которую необходимо решить");
    }

    @FXML
    void onSubmitApproxButtonClick() {
        reset();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (!fileIn.getText().equals("")) {
            BufferedReader br = openToRead(fileIn.getText());
            try {
                xs.setText(br.readLine());
                ys.setText(br.readLine());
                precision.setText(br.readLine());
            } catch (IOException e) {
                errorLabel.setText(String.format("Файл %s имеет неверный формат", fileIn.getText()));
            }
        }
        Approximation approximation = new Approximation();
        if (Arrays.stream(xs.getText().split(";")).map(String::trim).allMatch(s -> dv.validate(s, -Double.MAX_VALUE, Double.MAX_VALUE)) &&
                Arrays.stream(ys.getText().split(";")).map(String::trim).allMatch(s -> dv.validate(s, -Double.MAX_VALUE, Double.MAX_VALUE)) &&
                dv.validate(precision.getText(), -Double.MAX_VALUE, Double.MAX_VALUE)) {
            double[] xValues = Arrays.stream(xs.getText().split(";")).mapToDouble(s -> Double.parseDouble(s.trim().replace(",", "."))).toArray();
            double[] yValues = Arrays.stream(ys.getText().split(";")).mapToDouble(s -> Double.parseDouble(s.trim().replace(",", "."))).toArray();
            double p = Double.parseDouble(precision.getText().replace(",", "."));

            if (xValues.length != yValues.length) {
                errorLabel.setText("Введено разное количество точек по OX и OY");
                return;
            }
            if (!Arrays.equals(Arrays.stream(xValues).sorted().toArray(), xValues) || !(Arrays.stream(xValues).distinct().count() == xValues.length)) {
                errorLabel.setText("Точки по оси OX должны быть записаны в строго возрастающем порядке");
                return;
            }
            if (xValues.length < 5) {
                errorLabel.setText(String.format("Точек для аппроксимации должно быть минимум 8. Вы ввели: %d", xValues.length));
                return;
            }
            Map<Pair<Approximation.ApproximationType, Double>, Function<Double, Double>> variations = new HashMap<>();

            double[] linearApprox = approximation.linearApproximation(xValues, yValues, p);
            Function<Double, Double> phiLinear = x -> linearApprox[0] * x + linearApprox[1];
            variations.put(new Pair<>(Approximation.ApproximationType.LINEAR, approximation.standardDeviation(phiLinear, xValues, yValues)), phiLinear);
            logs.setText(logs.getText().concat(String.format("Линейная аппроксимация: Phi(x) = %fx + (%f), СКО: %f, Коэффициент корреляции Пирсона: %f, Цвет на графике: Черный\n", linearApprox[0], linearApprox[1], approximation.standardDeviation(phiLinear, xValues, yValues), approximation.correlation(xValues, yValues))));

            double[] squareApprox = approximation.squareApproximation(xValues, yValues, p);
            Function<Double, Double> phiSquare = x -> squareApprox[0] + x * squareApprox[1] + Math.pow(x, 2) * squareApprox[2];
            variations.put(new Pair<>(Approximation.ApproximationType.SQUARE, approximation.standardDeviation(phiSquare, xValues, yValues)), phiSquare);
            logs.setText(logs.getText().concat(String.format("Квадратичная аппроксимация: Phi(x) = %fx^2 + (%fx) + (%f), СКО: %f, Цвет на графике: Синий\n", squareApprox[2], squareApprox[1], squareApprox[0], approximation.standardDeviation(phiSquare, xValues, yValues))));

            double[] cubeApprox = approximation.cubeApproximation(xValues, yValues, p);
            Function<Double, Double> phiCube = x -> cubeApprox[0] + x * cubeApprox[1] + Math.pow(x, 2) * cubeApprox[2] + Math.pow(x, 3) * cubeApprox[3];
            variations.put(new Pair<>(Approximation.ApproximationType.CUBE, approximation.standardDeviation(phiCube, xValues, yValues)), phiCube);
            logs.setText(logs.getText().concat(String.format("Кубическая аппроксимация: Phi(x) = %fx^3 + (%f)x^2 + (%f)x + (%f), СКО: %f, Цвет на графике: Зеленый\n", cubeApprox[3], cubeApprox[2], cubeApprox[1], cubeApprox[0], approximation.standardDeviation(phiCube, xValues, yValues))));

            if (Arrays.stream(xValues).anyMatch(x -> x <= 0) || Arrays.stream(yValues).anyMatch(y -> y <= 0)) {
                logs.setText(logs.getText().concat("Степенная аппроксимация: Невозможно построить по введенным точкам\n"));
            } else {
                double[] powerApprox = approximation.powerApproximation(xValues, yValues, p);
                Function<Double, Double> phiPower = x -> powerApprox[1] * Math.pow(x, powerApprox[0]);
                variations.put(new Pair<>(Approximation.ApproximationType.POWER, approximation.standardDeviation(phiPower, xValues, yValues)), phiPower);
                logs.setText(logs.getText().concat(String.format("Степенная аппроксимация: Phi(x) = %fx^(%f), СКО: %f, Цвет на графике: Розовый\n", powerApprox[1], powerApprox[0], approximation.standardDeviation(phiPower, xValues, yValues))));
            }

            if (Arrays.stream(yValues).anyMatch(y -> y <= 0)) {
                logs.setText(logs.getText().concat("Экспоненциальная аппроксимация: Невозможно построить по введенным точкам\n"));
            } else {
                double[] expApprox = approximation.exponentialApproximation(xValues, yValues, p);
                Function<Double, Double> phiExp = x -> expApprox[1] * Math.exp(x * expApprox[0]);
                variations.put(new Pair<>(Approximation.ApproximationType.EXP, approximation.standardDeviation(phiExp, xValues, yValues)), phiExp);
                logs.setText(logs.getText().concat(String.format("Экспоненциальная аппроксимация: Phi(x) = %fe^(%fx), СКО: %f, Цвет на графике: Фиолетовый\n", expApprox[1], expApprox[0], approximation.standardDeviation(phiExp, xValues, yValues))));
            }

            if (Arrays.stream(xValues).anyMatch(x -> x <= 0)) {
                logs.setText(logs.getText().concat("Логарифмическая аппроксимация: Невозможно построить по введенным точкам\n"));
            } else {
                double[] logApprox = approximation.logApproximation(xValues, yValues, p);
                Function<Double, Double> phiLog = x -> logApprox[0] * Math.log(x) + logApprox[1];
                variations.put(new Pair<>(Approximation.ApproximationType.LOG, approximation.standardDeviation(phiLog, xValues, yValues)), phiLog);
                logs.setText(logs.getText().concat(String.format("Логарифмическая аппроксимация: Phi(x) = %fln(x) + (%f), СКО: %f, Цвет на графике: Оранжевый\n", logApprox[0], logApprox[1], approximation.standardDeviation(phiLog, xValues, yValues))));
            }

            variations.keySet().forEach(key -> {
                boolean axis = false;
                switch (key.getKey()) {
                    case LINEAR -> {
                        gc.setStroke(Color.BLACK);
                        axis = true;
                    }
                    case SQUARE -> gc.setStroke(Color.BLUE);
                    case CUBE -> gc.setStroke(Color.GREEN);
                    case POWER -> gc.setStroke(Color.VIOLET);
                    case EXP -> gc.setStroke(Color.PURPLE);
                    case LOG -> gc.setStroke(Color.ORANGE);
                }
                drawFunction(gc, variations.get(key), xValues[0], xValues[xValues.length - 1], Arrays.stream(yValues).min().getAsDouble(), Arrays.stream(yValues).max().getAsDouble(), axis);
            });
            //logs.setText(logs.getText().concat(String.format("Наилучшая аппроксимация: %s", Arrays.toString(variations.keySet().stream().filter(entry -> entry.getValue().equals(variations.keySet().stream().map(e -> e.getValue()).min(Comparator.naturalOrder()).get())).map(e -> e.getKey().toString()).toArray()))));
            gc.setStroke(Color.BLACK);

            IntStream.range(0, xValues.length).forEach(i -> {
                gc.setFill(Color.RED);
                gc.fillOval(convertXToCanvasX(xValues[i], xValues[0], xValues[xValues.length - 1]) - 2.5, convertYToCanvasY(yValues[i], Arrays.stream(yValues).min().getAsDouble(), Arrays.stream(yValues).max().getAsDouble()) - 2.5, 5, 5);
                gc.setFill(Color.BLACK);
            });

            double best = variations.keySet().stream().mapToDouble(Pair::getValue).min().getAsDouble();
            final String[] res = {""};
            variations.keySet().forEach(key -> {
                if (Math.abs(key.getValue() - best) < 0.000001)
                    res[0] = res[0].concat(key.getKey().toString() + ", ");
            });
            logs.setText(logs.getText().concat(String.format("Наилучшая аппроксимация: %s", res[0].substring(0, res[0].length() - 2))));

            if (!fileOut.getText().equals("")) {
                BufferedWriter bw = openToWrite(fileOut.getText());
                try {
                    bw.write(logs.getText());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    logs.setText("Произошла ошибка при записи в файл");
                }
                logs.setText(String.format("Результат работы успешно записан в файл %s", fileOut.getText()));
            }
        } else {
            errorLabel.setText("Координаты точек для аппроксимации и точность должны быть вещественными числами");
        }
    }

    void onSubmitInterpolButtonClick() {
        reset();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (!fileIn.getText().equals("")) {
            BufferedReader br = openToRead(fileIn.getText());
            try {
                xs.setText(br.readLine());
                ys.setText(br.readLine());
                precision.setText(br.readLine());
            } catch (IOException e) {
                errorLabel.setText(String.format("Файл %s имеет неверный формат", fileIn.getText()));
            }
        }

        if (functions.getSelectedToggle() != null && !((RadioButton) functions.getSelectedToggle()).getId().equals("f4")) {
            xs.setText("");
            ys.setText("");
            double left, right;
            int steps;
            if (dv.validate(leftBorder.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)
                    && dv.validate(rightBorder.getText().trim(), -Double.MAX_VALUE, Double.MAX_VALUE)
                    && dv.validate(interpolNum.getText().trim(), 0.0, Double.MAX_VALUE)) {
                left = Double.parseDouble(leftBorder.getText().trim().replace(",", "."));
                right = Double.parseDouble(rightBorder.getText().trim().replace(",", "."));
                steps = Integer.parseInt(interpolNum.getText());
            } else {
                errorLabel.setText("Введите корректные границы интервала и количество точек для интерполяции");
                return;
            }
            Function<Double, Double> f = null;
            switch (((RadioButton) functions.getSelectedToggle()).getId()) {
                case "f1" -> f = x -> 2 * Math.pow(x, 3) + 3.41 * Math.pow(x, 2) - 23.74 * x + 2.95;
                case "f2" -> f = x -> Math.sin(Math.exp(x));
                case "f3" -> f = x -> Math.tan(Math.sin(x) - Math.cos(x));
            }
            for (int i = 0; i < steps; i++) {
                xs.setText(xs.getText() + (left + (right - left) / (steps - 1) * i) + ";");
                ys.setText(ys.getText() + f.apply(left + (right - left) / (steps - 1) * i) + ";");
            }
            xs.setText(xs.getText().substring(0, xs.getText().length() - 1));
            ys.setText(ys.getText().substring(0, ys.getText().length() - 1));
        }


        Interpolation interpolation = new Interpolation();
        if (Arrays.stream(xs.getText().split(";")).map(String::trim).allMatch(s -> dv.validate(s, -Double.MAX_VALUE, Double.MAX_VALUE)) &&
                Arrays.stream(ys.getText().split(";")).map(String::trim).allMatch(s -> dv.validate(s, -Double.MAX_VALUE, Double.MAX_VALUE))) {
            double[] xValues = Arrays.stream(xs.getText().split(";")).mapToDouble(s -> Double.parseDouble(s.trim().replace(",", "."))).toArray();
            double[] yValues = Arrays.stream(ys.getText().split(";")).mapToDouble(s -> Double.parseDouble(s.trim().replace(",", "."))).toArray();

            if (xValues.length != yValues.length) {
                errorLabel.setText("Введено разное количество точек по OX и OY");
                return;
            }

            if (!Arrays.equals(Arrays.stream(xValues).sorted().toArray(), xValues) || !(Arrays.stream(xValues).distinct().count() == xValues.length)) {
                errorLabel.setText("Точки по оси OX должны быть записаны в строго возрастающем порядке");
                return;
            }

            if (xValues.length < 2) {
                errorLabel.setText(String.format("Точек для аппроксимации должно быть минимум 2. Вы ввели: %d", xValues.length));
                return;
            }

            double x_min, x_max, y_min, y_max;
            if (xValues[0] > 0)
                x_min = xValues[0] * 0.9;
            else
                x_min = xValues[0] * 1.1;

            if (xValues[xValues.length - 1] > 0)
                x_max = xValues[xValues.length - 1] * 1.1;
            else
                x_max = xValues[xValues.length - 1] * 0.9;

            if (interpolation.minFunctionValue(xValues, yValues) > 0)
                y_min = interpolation.minFunctionValue(xValues, yValues) * 0.9;
            else
                y_min = interpolation.minFunctionValue(xValues, yValues) * 1.1;

            if (interpolation.maxFunctionValue(xValues, yValues) > 0)
                y_max = interpolation.maxFunctionValue(xValues, yValues) * 1.1;
            else
                y_max = interpolation.maxFunctionValue(xValues, yValues) * 0.9;

            drawFunction(gc, interpolation.lagrange(xValues, yValues), x_min, x_max, y_min, y_max, true);
            gc.setStroke(Color.BLUE);
            drawFunction(gc, interpolation.newton(xValues, yValues), x_min, x_max, y_min, y_max, false);
            gc.setStroke(Color.BLACK);

            IntStream.range(0, xValues.length).forEach(i -> {
                gc.setFill(Color.RED);
                gc.fillOval(convertXToCanvasX(xValues[i], x_min, x_max) - 5, convertYToCanvasY(yValues[i], y_min, y_max) - 5, 10, 10);
                gc.setFill(Color.BLACK);
            });

            if (interpolation.checkStep(xValues))
                logs.setText(logs.getText() + "Построим таблицу конечных разностей:\n");
            else
                logs.setText(logs.getText() + "Построим таблицу разделенных разностей:\n");

            double[][] table = interpolation.newtonTable(xValues, yValues);
            for (int i = 0; i < table.length; i++) {
                logs.setText(logs.getText() + String.format("%d: %s\n", i, Arrays.toString(table[i])));
            }

            if (dv.validate(interpolX.getText(), -Double.MAX_VALUE, Double.MAX_VALUE)) {
                double x = Double.parseDouble(interpolX.getText().replace(",", "."));
                logs.setText(logs.getText() + String.format("L_%d(%f) = %f - значение, найденное по многочлену Лагранжа\n", xValues.length - 1, x, interpolation.lagrange(xValues, yValues).apply(x)));
                gc.setFill(Color.LIMEGREEN);
                if (interpolation.checkStep(xValues)) {
                    logs.setText(logs.getText() + String.format("N_%d(%f) = %f - значение, найденное по многочлену Ньютона (с конечными разностями)\n", xValues.length - 1, x, interpolation.newtonSameStep(xValues, yValues).apply(x)));
                    gc.fillOval(convertXToCanvasX(Double.parseDouble(interpolX.getText().replace(",", ".")), x_min, x_max) - 5, convertYToCanvasY(interpolation.newtonSameStep(xValues, yValues).apply(x), y_min, y_max) - 5, 10, 10);
                } else {
                    logs.setText(logs.getText() + String.format("N_%d(%f) = %f - значение, найденное по многочлену Ньютона (с разделенными разностями)\n", xValues.length - 1, x, interpolation.newton(xValues, yValues).apply(x)));
                    gc.fillOval(convertXToCanvasX(x, x_min, x_max) - 5, convertYToCanvasY(interpolation.newton(xValues, yValues).apply(x), y_min, y_max) - 5, 10, 10);
                }

                if (interpolation.checkStirling(xValues, x)) {
                    logs.setText(logs.getText() + String.format("S_%d(%f) = %f - значение, найденное по многочлену Стирлинга\n", xValues.length - 1, x, interpolation.stirling(xValues, yValues).apply(x)));
                } else {
                    logs.setText(logs.getText() + String.format("Не выполнены условия для построения многочлена Стирлинга (Узлы равностоящие: %b, Нечетное число узлов в разбиении: %b, t: %f)\n", interpolation.checkStep(xValues), xValues.length % 2 == 1, (x - xValues[(xValues.length - 1) / 2]) / (xValues[1] - xValues[0])));
                }

                if (interpolation.checkBessel(xValues, x)) {
                    logs.setText(logs.getText() + String.format("B_%d(%f) = %f - значение, найденное по многочлену Бесселя\n", xValues.length - 1, x, interpolation.bessel(xValues, yValues).apply(x)));
                } else {
                    logs.setText(logs.getText() + String.format("Не выполнены условия для построения многочлена Бесселя (Узлы равностоящие: %b, Четное число узлов в разбиении: %b, t: %f)\n", interpolation.checkStep(xValues), xValues.length % 2 == 0, (x - xValues[(xValues.length - 1) / 2]) / (xValues[1] - xValues[0])));
                }
                logs.setText(logs.getText() + interpolation.usedValuesFromTable + "\n");
                gc.setFill(Color.BLACK);
            }

            if (!fileOut.getText().equals("")) {
                BufferedWriter bw = openToWrite(fileOut.getText());
                try {
                    bw.write(logs.getText());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    logs.setText("Произошла ошибка при записи в файл");
                }
                logs.setText(String.format("Результат работы успешно записан в файл %s", fileOut.getText()));
            }
        } else {
            errorLabel.setText("Координаты точек для интерполяции и точность должны быть вещественными числами");
        }
    }

    void onSubmitDiffButtonClick() {
        reset();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (dv.validate(precision.getText(), Double.MIN_VALUE, Double.MAX_VALUE)
                && dv.validate(leftBorder.getText(), -Double.MAX_VALUE, Double.MAX_VALUE)
                && dv.validate(rightBorder.getText(), -Double.MAX_VALUE, Double.MAX_VALUE)
                && dv.validate(step.getText(), Double.MIN_VALUE, Double.MAX_VALUE)
                && dv.validate(initY.getText(), -Double.MAX_VALUE, Double.MAX_VALUE)) {
            Double p = Double.parseDouble(precision.getText().replace(",", "."));
            Double x0 = Double.parseDouble(leftBorder.getText().replace(",", "."));
            Double xN = Double.parseDouble(rightBorder.getText().replace(",", "."));
            Double h = Double.parseDouble(step.getText().replace(",", "."));
            Double y0 = Double.parseDouble(initY.getText().replace(",", "."));

            Function<Double[], Double> f = null;
            Function<Double, Double> correctAnswer = null;

            if (diff.getSelectedToggle() != null) {
                switch (((RadioButton) diff.getSelectedToggle()).getId()) {
                    case "d1" -> {
                        f = d -> d[1] + (1 + d[0]) * Math.pow(d[1], 2);
                        correctAnswer = x -> 1 / ((1 / y0 + x0) * Math.exp(x0) * Math.exp(-x) - x);
                    }
                    case "d2" -> {
                        f = d -> d[1] + d[0];
                        correctAnswer = x -> (y0 + x0 + 1) * Math.exp(-x0) * Math.exp(x) - x - 1;
                    }
                    case "d3" -> {
                        f = d -> d[1] + Math.sin(d[0]);
                        correctAnswer = x -> (y0 + (Math.sin(x0) + Math.cos(x0)) * 0.5) * Math.exp(-x0) * Math.exp(x) - 0.5 * Math.sin(x) - 0.5 * Math.cos(x);
                    }
                }

                DifferentialEquation de = new DifferentialEquation();

                double[] eulerY = de.monoStep(DifferentialEquation.MonoStepType.EULER, f, x0, xN, y0, h, p);
                double[] rungeY = de.monoStep(DifferentialEquation.MonoStepType.RUNGE, f, x0, xN, y0, h, p);
                double[] adamsY = de.multiStep(DifferentialEquation.MultiStepType.ADAMS, f, correctAnswer, x0, xN, y0, h, p);

                AtomicReference<Double> y_min = new AtomicReference<>(Math.min(Arrays.stream(eulerY).min().getAsDouble(), Arrays.stream(rungeY).min().getAsDouble()));
                AtomicReference<Double> y_max = new AtomicReference<>(Math.max(Arrays.stream(eulerY).max().getAsDouble(), Arrays.stream(rungeY).max().getAsDouble()));
                AtomicReference<Double> x_min = new AtomicReference<>(x0);
                AtomicReference<Double> x_max = new AtomicReference<>(xN);

                if (x_min.get() > 0)
                    x_min.set(x_min.get() * 0.9);
                else
                    x_min.set(x_min.get() * 1.1);

                if (x_max.get() > 0)
                    x_max.set(x_max.get() * 1.1);
                else
                    x_max.set(x_max.get() * 0.9);

                if (y_min.get() > 0)
                    y_min.set(y_min.get() * 0.9);
                else
                    y_min.set(y_min.get() * 1.1);

                if (y_max.get() > 0)
                    y_max.set(y_max.get() * 1.1);
                else
                    y_max.set(y_max.get() * 0.9);

                drawFunction(gc, correctAnswer, x_min.get(), x_max.get(), y_min.get(), y_max.get(), true);

                logs.setText(logs.getText() + "Вычисленные значения:\n");
                for (int i = 0; i < 3; i++) {
                    AtomicInteger counter = new AtomicInteger(0);
                    double[] ys = new double[0];
                    switch (i) {
                        case 0 -> {
                            logs.setText(logs.getText() + "Метод Эйлера:\n");
                            gc.setFill(Color.RED);
                            ys = eulerY;
                        }
                        case 1 -> {
                            logs.setText(logs.getText() + "Метод Рунге-Кутта:\n");
                            gc.setFill(Color.BLUE);
                            ys = rungeY;
                        }
                        case 2 -> {
                            logs.setText(logs.getText() + "Метод Адамса:\n");
                            gc.setFill(Color.LIMEGREEN);
                            if (adamsY != null) ys = adamsY;
                        }
                    }
                    int finalI = i;
                    Function<Double, Double> finalCorrectAnswer = correctAnswer;
                    Arrays.stream(ys).forEach(e -> {
                        if (finalI == 2)
                            logs.setText(logs.getText() + String.format("Точное решение y(%f) = %f, ", x0 + h * counter.get(), finalCorrectAnswer.apply(x0 + h * counter.get())));
                        logs.setText(logs.getText() + String.format("y(%f) = %f\n", x0 + h * counter.get(), e));

                        gc.fillOval(convertXToCanvasX(x0 + h * counter.getAndIncrement(), x_min.get(), x_max.get()) - 5, convertYToCanvasY(e, y_min.get(), y_max.get()) - 5, 10, 10);
                    });
                    switch (i) {
                        case 0 ->
                                logs.setText(logs.getText() + String.format("Количество интервалов в разбиении для правила Рунге: %s, h: %f\n%s", de.eulerRunge, h / de.rungeRunge, de.eulerRungeResults));
                        case 1 ->
                                logs.setText(logs.getText() + String.format("Количество интервалов в разбиении для правила Рунге: %s, h: %f\n%s", de.rungeRunge, h / de.rungeRunge, de.rungeRungeResults));
                    }
                    counter.set(0);
                }
                gc.setFill(Color.BLACK);
                logs.setText(logs.getText() + String.format("epsilon = %f", de.adamsCheck));

                logs.setText(logs.getText() + "Черный цвет: точное решение\nКрасный цвет: значения, полученные по формуле Эйлера\nСиний цвет: значения, полученные по формуле Рунге-Кутта\n");
                if (adamsY == null) {
                    logs.setText(logs.getText() + "Не удалось найти решение по методу Адамса, так как в разбиении слишком мало узлов\n");
                } else logs.setText(logs.getText() + "Зеленый цвет: значения, полученные по формуле Адамса\n");
            } else errorLabel.setText("Выберите целевую функцию");
        } else errorLabel.setText("Обнаружен некорректный ввод в одном из полей");
    }

    BufferedReader openToRead(String fp) {
        File filepath = new File(fp);
        try {
            return new BufferedReader(new FileReader(filepath));
        } catch (IOException e) {
            errorLabel.setText(String.format("Файл %s не найден", fp));
            return null;
        }
    }

    BufferedWriter openToWrite(String fp) {
        File file = new File(fp);
        try {
            if (file.exists() || file.createNewFile())
                return new BufferedWriter(new FileWriter(file));
            else {
                logs.setText(String.format("Произошла ошибка во время записи результата в файл %s", file.getName()));
                return null;
            }
        } catch (IOException e) {
            logs.setText(String.format("Произошла ошибка во время записи результата в файл %s", file.getName()));
            return null;
        }
    }

    void drawFunction(GraphicsContext gc, Function<Double, Double> f, Double left, Double right, Double y_min, Double y_max, boolean axis) {
        List<Double> xs = Stream.iterate(left, i -> i + (right - left) / 100).limit(100).toList();
        List<Double> ys = xs.stream().map(f).toList();

        if (y_max.equals(y_min)) {
            y_min -= 0.001;
            y_max += 0.001;
        }

        if (axis) {
            gc.strokeLine(0, y_max / (y_max - y_min) * 500, 500, y_max / (y_max - y_min) * 500);
            gc.fillText(left.toString(), 5, y_max / (y_max - y_min) * 500 - 2);
            gc.fillText(right.toString(), 480, y_max / (y_max - y_min) * 500 - 2);
            if (Math.signum(left) != Math.signum(right)) {
                gc.strokeLine(Math.abs(left) / (right - left) * 500, 0, Math.abs(left) / (right - left) * 500, 500);
                gc.fillText(Double.toString(y_max), Math.abs(left) / (right - left) * 500 + 2, 10);
                gc.fillText(Double.toString(y_min), Math.abs(left) / (right - left) * 500 + 2, 495);
            } else {
                gc.fillText(Double.toString(y_max), 250, 10);
                gc.fillText(Double.toString(y_min), 250, 495);
            }
        }

        double startX = convertXToCanvasX(xs.get(0), left, right);
        double startY = convertYToCanvasY(ys.get(0), y_min, y_max);
        for (int i = 1; i < xs.size(); i++) {
            double endX = convertXToCanvasX(xs.get(i), left, right);
            double endY = convertYToCanvasY(ys.get(i), y_min, y_max);
            gc.strokeLine(startX, startY, endX, endY);
            startX = endX;
            startY = endY;
        }
    }

    void drawFunction(GraphicsContext gc, Function<Double, Double> f, Double left, Double right, Double res) {
        List<Double> xs = Stream.iterate(left, i -> i + (right - left) / 100).limit(100).toList();
        List<Double> ys = xs.stream().map(f).toList();

        double y_max = ys.stream().max(Comparator.naturalOrder()).get();
        double y_min = ys.stream().min(Comparator.naturalOrder()).get();

        gc.strokeLine(0, y_max / (y_max - y_min) * 500, 500, y_max / (y_max - y_min) * 500);
        gc.fillText(left.toString(), 5, y_max / (y_max - y_min) * 500 - 2);
        gc.fillText(right.toString(), 480, y_max / (y_max - y_min) * 500 - 2);
        if (Math.signum(left) != Math.signum(right)) {
            gc.strokeLine(Math.abs(left) / (right - left) * 500, 0, Math.abs(left) / (right - left) * 500, 500);
            gc.fillText(Double.toString(y_max), Math.abs(left) / (right - left) * 500 + 2, 10);
            gc.fillText(Double.toString(y_min), Math.abs(left) / (right - left) * 500 + 2, 495);
        } else {
            gc.fillText(Double.toString(y_max), 250, 10);
            gc.fillText(Double.toString(y_min), 250, 495);
        }


        double startX = convertXToCanvasX(xs.get(0), left, right);
        double startY = convertYToCanvasY(ys.get(0), y_min, y_max);
        for (int i = 1; i < xs.size(); i++) {
            double endX = convertXToCanvasX(xs.get(i), left, right);
            double endY = convertYToCanvasY(ys.get(i), y_min, y_max);
            gc.strokeLine(startX, startY, endX, endY);
            startX = endX;
            startY = endY;
        }

        if (res != null) {
            gc.setFill(Color.RED);
            gc.fillOval(convertXToCanvasX(res, left, right) - 2.5, convertYToCanvasY(f.apply(res), y_min, y_max) - 2.5, 5, 5);
            gc.setFill(Color.BLACK);
        }
    }


    void drawSystem(Function<double[], Double> f1, Function<double[], Double> f2, double[] res) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 500, 500);

        double x_min = res[0] - 10;
        double x_max = res[0] + 10;
        double y_min = res[1] - 10;
        double y_max = res[1] + 10;

        gc.strokeLine(0, y_max / (y_max - y_min) * 500, 500, y_max / (y_max - y_min) * 500);
        gc.fillText(String.valueOf(x_min), 5, y_max / (y_max - y_min) * 500 - 2);
        gc.fillText(String.valueOf(x_max), 480, y_max / (y_max - y_min) * 500 - 2);
        if (Math.signum(x_min) != Math.signum(x_max)) {
            gc.strokeLine(Math.abs(x_min) / (x_max - x_min) * 500, 0, Math.abs(x_min) / (x_max - x_min) * 500, 500);
            gc.fillText(Double.toString(y_max), Math.abs(x_min) / (x_max - x_min) * 500 + 2, 10);
            gc.fillText(Double.toString(y_min), Math.abs(x_min) / (x_max - x_min) * 500 + 2, 495);
        } else {
            gc.fillText(Double.toString(y_max), 250, 10);
            gc.fillText(Double.toString(y_min), 250, 495);
        }

        double precision = 0.01;
        for (double i = x_min; i <= x_max; i += (x_max - x_min) / 1000) {
            for (double j = y_min; j <= y_max; j += (y_max - y_min) / 1000) {
                if (Math.abs(f1.apply(new double[]{i, j})) < precision) {
                    gc.fillOval(convertXToCanvasX(i, x_min, x_max), convertYToCanvasY(j, y_min, y_max), 4, 4);
                }
                if (Math.abs(f2.apply(new double[]{i, j})) < precision) {
                    gc.fillOval(convertXToCanvasX(i, x_min, x_max), convertYToCanvasY(j, y_min, y_max), 4, 4);
                }
            }
        }

        gc.setFill(Color.RED);
        gc.fillOval(convertXToCanvasX(res[0], x_min, x_max) - 2.5, convertYToCanvasY(res[1], y_min, y_max) - 2.5, 5, 5);
        gc.setFill(Color.BLACK);
    }


    double convertXToCanvasX(double x, double x_min, double x_max) {
        if (Math.signum(x_max) != Math.signum(x_min)) {
            return 500 * ((Math.abs(x_min) + x) / (x_max - x_min));
        } else
            return 500 * ((x - x_min) / (x_max - x_min));
    }


    double convertYToCanvasY(double y, double y_min, double y_max) {
        return 500 * ((y_max - y) / (y_max - y_min));
    }


    boolean checkInterval(Function<Double, Double> f, Double left, Double right) {
        List<Double> signs = Stream.iterate(left, i -> i + (right - left) / 1000).limit(1000).map(f).map(Math::signum).toList();
        List<Double> s;
        if (signs.get(0) > 0)
            s = signs.stream().sorted(Comparator.reverseOrder()).toList();
        else
            s = signs.stream().sorted().toList();
        return new HashSet<>(signs).containsAll(List.of(-1.0, 1.0)) && signs.equals(s);
    }

    void reset() {
        logs.setText("");
        errorLabel.setText("");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 500, 500);
        gc.setLineWidth(3);
    }
}