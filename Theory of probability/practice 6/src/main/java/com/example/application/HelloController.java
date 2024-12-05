package com.example.application;

import com.example.math.IntervalStatisticalPair;
import com.example.math.MathStatistics;
import com.example.math.OrderedPair;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private static final ArrayList<Number> values = new ArrayList<>(List.of(1.07, 1.59, -1.49, -0.1, 0.11, 1.18, 0.35, -0.73, 1.07, 0.31, -0.26, -1.2, -0.35, 0.73, 1.01, -0.12, 0.28, -1.32, -1.1, -0.26));
    private final List<? extends Number> headers = MathStatistics.statisticalSeries(values).stream().map(OrderedPair::getA).toList();
    private final List<Long> occurrence = MathStatistics.statisticalSeries(values).stream().map(OrderedPair::getB).toList();
    private final double precision = Math.max(Math.abs(MathStatistics.min(values)), Math.abs(MathStatistics.max(values)));
    private final List<IntervalStatisticalPair> intervalStatisticalSeries = MathStatistics.intervalStatisticalSeries(values).stream().toList();

    @FXML
    private Label sampling;
    @FXML
    private Label variationalSeries;
    @FXML
    private Label range;
    @FXML
    private Label extreme;
    @FXML
    private TableView<List<? extends Number>> statisticalSeries;
    @FXML
    private Label moments;
    @FXML
    private Canvas distributionFunction;
    @FXML
    private Canvas polygon;
    @FXML
    private Canvas histogram;
    @FXML
    private TableView<List<? extends Number>> ISSTable;
    @FXML
    private Label distribution;

    private void drawDistributionFunction(GraphicsContext gc) {
        gc.strokeLine(0, 200, 330, 200);
        gc.strokeLine(165, 0, 165, 220);
        long n = 0;
        for (int i = 0; i < headers.size(); i++) {
            n += occurrence.get(i);
            if (headers.get(i).doubleValue() < 0) {
                gc.strokeLine(15 + (precision + headers.get(i).doubleValue()) / precision * 150, 195, 15 + (precision + headers.get(i).doubleValue()) / precision * 150, 205);
                if (i + 1 != headers.size()) {
                    gc.strokeLine(15 + (precision + headers.get(i).doubleValue()) / precision * 150, 200 - (double) n / (values.size() + 1) * 200, 15 + (precision + headers.get(i + 1).doubleValue()) / precision * 150, 200 - (double) n / (values.size() + 1) * 200);
                } else {
                    gc.strokeLine(15 + (precision + headers.get(i).doubleValue()) / precision * 150, 200 - (double) n / (values.size() + 1) * 200, 330, 200 - (double) n / (values.size() + 1) * 200);
                }
            } else {
                gc.strokeLine(165 + headers.get(i).doubleValue() / precision * 150, 195, 165 + headers.get(i).doubleValue() / precision * 150, 205);
                if (i + 1 != headers.size()) {
                    gc.strokeLine(165 + headers.get(i).doubleValue() / precision * 150, 200 - (double) n / (values.size() + 1) * 200, 165 + headers.get(i + 1).doubleValue() / precision * 150, 200 - (double) n / (values.size() + 1) * 200);
                } else {
                    gc.strokeLine(165 + headers.get(i).doubleValue() / precision * 150, 200 - (double) n / (values.size() + 1) * 200, 330, 200 - (double) n / (values.size() + 1) * 200);

                }
            }
        }
        for (int i = 0; i <= values.size(); i++) {
            if (i != 0 && i % 2 == 0)
                gc.fillText(String.valueOf((double) i / values.size()), 172, 200 - (double) i / (values.size() + 1) * 200 + 5);
            gc.strokeLine(160, 200 - (double) i / (values.size() + 1) * 200, 170, 200 - (double) i / (values.size() + 1) * 200);
        }
    }

    private void drawPolygon(GraphicsContext gc) {
        gc.strokeLine(0, 200, 330, 200);
        gc.strokeLine(165, 0, 165, 220);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).doubleValue() < 0) {
                gc.strokeLine(15 + (precision + headers.get(i).doubleValue()) / precision * 150, 195, 15 + (precision + headers.get(i).doubleValue()) / precision * 150, 205);
                if (i + 1 != headers.size()) {
                    gc.strokeLine(15 + (precision + headers.get(i).doubleValue()) / precision * 150, 200 - (double) occurrence.get(i) / (occurrence.stream().distinct().count() + 1) * 200, 15 + (precision + headers.get(i + 1).doubleValue()) / precision * 150, 200 - (double) occurrence.get(i + 1) / (occurrence.stream().distinct().count() + 1) * 200);
                }
            } else {
                gc.strokeLine(165 + headers.get(i).doubleValue() / precision * 150, 195, 165 + headers.get(i).doubleValue() / precision * 150, 205);
                if (i + 1 != headers.size()) {
                    gc.strokeLine(165 + headers.get(i).doubleValue() / precision * 150, 200 - (double) occurrence.get(i) / (occurrence.stream().distinct().count() + 1) * 200, 165 + headers.get(i + 1).doubleValue() / precision * 150, 200 - (double) occurrence.get(i + 1) / (occurrence.stream().distinct().count() + 1) * 200);
                }
            }
        }
        for (int i = 0; i < occurrence.stream().distinct().count() + 1; i++) {
            gc.fillText(String.valueOf((double) i / values.size()), 170, 200 - (double) i / (occurrence.stream().distinct().count() + 1) * 200 - 10);
            gc.strokeLine(160, 200 - (double) i / (occurrence.stream().distinct().count() + 1) * 200, 170, 200 - (double) i / (occurrence.stream().distinct().count() + 1) * 200);
        }
    }

    private void drawHistogram(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.strokeLine(0, 200, 330, 200);
        double precision = Math.max(Math.abs(intervalStatisticalSeries.get(0).getA().getA()), Math.abs(intervalStatisticalSeries.get(intervalStatisticalSeries.size() - 1).getA().getB()));
        intervalStatisticalSeries.forEach(i -> {
            if (i.getA().getA() < 0) {
                gc.setFill(Color.BLACK);
                gc.fillText(i.getB().toString(), 15 + (precision + i.getA().getA()) / precision * 150 + 20, 200 - (double) i.getB() / (values.size() + 1) * 200 - 5);
                gc.setFill(Color.BLUE);
                if (i.getA().getB() < 0) {
                    gc.fillRect(15 + (precision + i.getA().getA()) / precision * 150 + 5, 200 - (double) i.getB() / (values.size() + 1) * 200, (precision + i.getA().getB()) / precision * 150 - ((precision + i.getA().getA()) / precision * 150) - 5, (double) i.getB() / (values.size() + 1) * 200);
                } else {
                    gc.fillRect(15 + (precision + i.getA().getA()) / precision * 150 + 5, 200 - (double) i.getB() / (values.size() + 1) * 200, (i.getA().getB() - i.getA().getA()) / precision * 150 - 5, (double) i.getB() / (values.size() + 1) * 200);
                }
            } else {
                gc.setFill(Color.BLACK);
                gc.fillText(i.getB().toString(), 165 + i.getA().getA() / precision * 150 + 20, 200 - (double) i.getB() / (values.size() + 1) * 200 - 5);
                gc.setFill(Color.BLUE);
                gc.fillRect(165 + i.getA().getA() / precision * 150 + 5, 200 - (double) i.getB() / (values.size() + 1) * 200, i.getA().getB() / precision * 150 - (i.getA().getA() / precision * 150) - 5, (double) i.getB() / (values.size() + 1) * 200);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringBuilder sb = new StringBuilder();

        values.stream().map(i -> i.toString() + " ").forEach(sb::append);
        sampling.setText(sb.toString().trim());

        sb.setLength(0);
        MathStatistics.variationalSeries(values).forEach(i -> sb.append(i).append(" <= "));
        variationalSeries.setText(sb.substring(0, sb.length() - 4));

        range.setText(MathStatistics.range(values).toString());

        extreme.setText(String.format("Экстремальные значения:\n\tМаксимум: %.2f\n\tМинимум: %.2f\n", MathStatistics.max(values), MathStatistics.min(values)));

        for (int i = 0; i < headers.size(); i++) {
            final int colIndex = i;
            TableColumn<List<? extends Number>, Number> column = new TableColumn<>(headers.get(i).toString());
            column.setCellValueFactory(data -> {
                List<? extends Number> row = data.getValue();
                Number cell = row.get(colIndex);
                return new ReadOnlyIntegerWrapper(cell.intValue());
            });
            statisticalSeries.getColumns().add(column);
        }
        statisticalSeries.getItems().add(occurrence);

        moments.setText(String.format("Числовые характеристики:\n\tВыборочное среднее: %f\n\tВыборочная дисперсия: %f\n\tИсправленная дисперсия: %f\n\tВыборочное среднеквадратическое отклонение: %f\n\tИсправленное среднеквадратическое отклонение: %f\n", MathStatistics.sampleMean(values), MathStatistics.sampleVariance(values), MathStatistics.unbiasedSampleVariance(values), MathStatistics.sampleStandardDeviation(values), MathStatistics.correctedSampleStandardDeviation(values)));

        drawDistributionFunction(distributionFunction.getGraphicsContext2D());
        drawPolygon(polygon.getGraphicsContext2D());
        drawHistogram(histogram.getGraphicsContext2D());

        for (int i = 0; i < intervalStatisticalSeries.size(); i++) {
            final int colIndex = i;
            TableColumn<List<? extends Number>, Number> column = new TableColumn<>(intervalStatisticalSeries.get(i).getA().toString());
            column.setCellValueFactory(data -> {
                List<? extends Number> row = data.getValue();
                Number cell = row.get(colIndex);
                return new ReadOnlyIntegerWrapper(cell.intValue());
            });
            ISSTable.getColumns().add(column);
        }
        ISSTable.getItems().add(intervalStatisticalSeries.stream().map(OrderedPair::getB).toList());

        sb.setLength(0);
        long n = 0;
        for (int i = 0; i < headers.size(); i++){
            n += occurrence.get(i);
            if (i == 0){
                sb.append(String.format("{ 0, при x <= %.2f;\n", headers.get(i).doubleValue()));
            }
            if (i + 1 < headers.size()){
                sb.append(String.format("%.2f, при %.2f < x <= %.2f;\n", (double) n / values.size(), headers.get(i).doubleValue(), headers.get(i + 1).doubleValue()));
            }
            if (i + 1 == headers.size()){
                sb.append(String.format("1, при x > %.2f", headers.get(i).doubleValue()));
            }
        }
        distribution.setText(String.format("Функция распределения:\nF(x)=%s", sb));
    }
}