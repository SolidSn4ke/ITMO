package com.example.dragonline;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import math.Complex;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private VBox controls;
    @FXML
    private ComboBox<String> selector;
    @FXML
    private Label iterLabel;
    @FXML
    private TextField iterInput;
    @FXML
    private Label reMinLabel;
    @FXML
    private TextField reMinInput;
    @FXML
    private Label reMaxLabel;
    @FXML
    private TextField reMaxInput;
    @FXML
    private Label imMinLabel;
    @FXML
    private TextField imMinInput;
    @FXML
    private Label imMaxLabel;
    @FXML
    private TextField imMaxInput;
    @FXML
    private Label startReLabel;
    @FXML
    private TextField startReInput;
    @FXML
    private Label startImLabel;
    @FXML
    private TextField startImInput;
    @FXML
    private Label errorLabel;
    @FXML
    private Button generateButton;
    @FXML
    private Canvas canvas;

    private GraphicsContext gc = null;

    @FXML
    protected void initialize() {
        selector.getItems().addAll("Множество Мандельброта", "Множество Жюлиа", "Ломаная Дракона");
        selector.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            controls.getChildren().removeAll(controls.getChildren());
            switch (newValue) {
                case "Ломаная Дракона" ->
                        controls.getChildren().addAll(selector, iterLabel, iterInput, generateButton, errorLabel);
                case "Множество Мандельброта" ->
                        controls.getChildren().addAll(selector, iterLabel, iterInput, reMinLabel, reMinInput, reMaxLabel, reMaxInput, imMinLabel, imMinInput, imMaxLabel, imMaxInput, generateButton, errorLabel);
                case "Множество Жюлиа" ->
                        controls.getChildren().addAll(selector, iterLabel, iterInput, reMinLabel, reMinInput, reMaxLabel, reMaxInput, imMinLabel, imMinInput, imMaxLabel, imMaxInput, startReLabel, startReInput, startImLabel, startImInput, generateButton, errorLabel);
            }
        });
        selector.getSelectionModel().selectFirst();
    }

    @FXML
    protected void onGenerateButtonClick() {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (iterInput.getText().matches("[1-9][0-9]*")) {
            errorLabel.setText("");
            switch (selector.getValue()) {
                case "Ломаная Дракона" -> drawDragon(200, 200, 500, 500, Integer.parseInt(iterInput.getText()));
                case "Множество Мандельброта" -> {
                    ArrayList<TextField> inputs = new ArrayList<>(List.of(reMinInput, reMaxInput, imMinInput, imMaxInput));
                    errorLabel.setText("");
                    inputs.forEach(textField -> {
                        if (!textField.getText().matches("-?[1-9][0-9]*[.,][0-9]+|-?0[.,][0-9]+") &&
                        !textField.getText().matches("-?[1-9][0-9]*|0")){
                            errorLabel.setText("Неверно указаны границы для отображения множества Мандельброта");
                        }
                    });
                    if (errorLabel.getText().matches("")){
                        drawMandelbrot(Double.parseDouble(reMinInput.getText()), Double.parseDouble(reMinInput.getText()), Double.parseDouble(imMinInput.getText()), Double.parseDouble(imMaxInput.getText()), Integer.parseInt(iterInput.getText()));
                    }
                }
                case "Множество Жюлиа" -> {
                    ArrayList<TextField> inputs = new ArrayList<>(List.of(reMinInput, reMaxInput, imMinInput, imMaxInput, startReInput, startImInput));
                    errorLabel.setText("");
                    inputs.forEach(textField -> {
                        if (!textField.getText().matches("-?[1-9][0-9]*[.,][0-9]+|-?0[.,][0-9]+") &&
                                !textField.getText().matches("-?[1-9][0-9]*|0")){
                            errorLabel.setText("Неверно указаны границы для отображения множества Жюлиа");
                        }
                    });
                    drawJulia(Double.parseDouble(reMinInput.getText()), Double.parseDouble(reMinInput.getText()), Double.parseDouble(imMinInput.getText()), Double.parseDouble(imMaxInput.getText()), new Complex(Double.parseDouble(startReInput.getText()), Double.parseDouble(startImInput.getText())), Integer.parseInt(iterInput.getText()));
                }

            }
        } else {
            errorLabel.setText("Количество итераций должно быть натуральным числом");
        }
    }

    private void drawDragon(double x1, double y1, double x2, double y2, int n) {
        double xn, yn;
        if (n > 0)
        {
            xn = (x1 + x2) / 2 + (y2 - y1) / 2;
            yn = (y1 + y2) / 2 - (x2 - x1) / 2;
            drawDragon(x2, y2, xn, yn, n - 1);
            drawDragon(x1, y1, xn, yn, n - 1);
        }
        if (n == 0) {//Приступаем к рисованию прямых
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void drawMandelbrot(double reMin, double reMax, double imMin, double imMax, int n) {
        double precision = Math.max((reMax - reMin) / canvas.getWidth(), (imMax - imMin) / canvas.getHeight());
        gc.setFill(Color.BLACK);
        for (double c = reMin, xR = 0; xR < canvas.getWidth(); c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvas.getHeight(); ci = ci + precision, yR++) {
                int convergenceValue = checkConvergence(new Complex(c, ci), new Complex(0, 0), n);
                double t1 = (double) convergenceValue / n;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (convergenceValue != n) {
                    gc.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    gc.setFill(Color.SKYBLUE);
                }
                gc.fillRect(xR, yR, 1, 1);
            }
        }
    }

    private void drawJulia(double reMin, double reMax, double imMin, double imMax, Complex z, int n){
        int converganceValue;
        double precision = Math.max((reMax - reMin) / canvas.getWidth(), (imMax - imMin) / canvas.getHeight());
        for (double c = reMin, xR = 0; xR < canvas.getWidth(); c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvas.getHeight(); ci = ci + precision, yR++) {
                converganceValue = checkConvergence(z, new Complex(c, ci), n);
                double t1 = (double) converganceValue / n;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (converganceValue != n) {
                    gc.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    gc.setFill(Color.BLUE);
                }
                gc.fillRect(xR, yR, 1, 1);
            }
        }
    }

    private int checkConvergence(Complex c, Complex z, int convergenceSteps) {
        for (int i = 0; i < convergenceSteps; i++) {
            z = z.mul(z).add(c);
            if (z.abs() >= 4.0) {
                return i;
            }
        }
        return convergenceSteps;
    }
}