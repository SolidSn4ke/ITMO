package org.example.parsers;

import org.example.system.io.InputManager;
import org.example.system.io.OutputManager;

import java.math.BigDecimal;

/**
 * Интерфейс для определения пользовательского ввода для дробных чисел типа double/Double
 */
public interface DoubleParser {
    /**
     * Определить пользовательский ввод
     *
     * @param source         Объект класса InputManager для работы с конкретным источников
     * @param context        Строка, которая будет выведена в сообщении в консоль. Указывает на поле, которое заполняет пользователь
     * @param mayBeNull      Логическое значение, которое определяющет может ли данный метод вернуть null значение
     * @param mayBeLessThan0 Логическое значение, которое определяющет может ли данный метод вернуть числа меньше 0
     * @return Введенное пользователем число
     */
    default Double parse(InputManager source, String context, Boolean mayBeNull, Boolean mayBeLessThan0) {
        OutputManager outputManager = new OutputManager();
        String constraint = " положительное";
        if (mayBeLessThan0) constraint = "";

        messageDoubleInput(outputManager, context, constraint);
        String input;
        String regex;
        if (mayBeLessThan0) {
            regex = "-?(?:[0-9](?:[,.]\\d+|)|[1-9]\\d*[,.]?\\d+)";
        } else {
            regex = "(?:[0-9](?:[,.]\\d+|)|[1-9]\\d*[,.]?\\d+)";
        }
        outputManager.messageInputPrompt();
        while (true) {
            input = source.tryReadLine();
            if (input == null){
                outputManager.print("Достигнут конец файла. Ввод продолжится с клавиатуры");
                outputManager.messageInputPrompt();
                continue;
            } else input = input.strip();
            if (!input.equals("")) {
                if (mayBeNull && input.equals("skip")) {
                    return null;
                }
                if (input.matches(regex)) {
                    input = input.replace(",", ".");
                    if (new BigDecimal(input).abs().compareTo(new BigDecimal(Double.MAX_VALUE)) < 0
                            && (mayBeLessThan0 || Double.parseDouble(input) >= 0))
                        return Double.parseDouble(input);
                } else {
                    messageWrongDoubleInput(outputManager, constraint);
                    outputManager.messageInputPrompt();
                }
            }
        }
    }

    default boolean check(String s, Boolean mayBeNull, Boolean mayBeLessThan0) {
        String regex;
        if (mayBeLessThan0) {
            regex = "-?(?:[0-9](?:[,.]\\d+|)|[1-9]\\d*[,.]?\\d+)";
        } else {
            regex = "(?:[0-9](?:[,.]\\d+|)|[1-9]\\d*[,.]?\\d+)";
        }
        if (mayBeNull && s == null) {
            return true;
        }
        if (s != null && s.matches(regex)) {
            s = s.replace(",", ".");
            return new BigDecimal(s).abs().compareTo(new BigDecimal(Double.MAX_VALUE)) < 0
                    && (mayBeLessThan0 || Double.parseDouble(s) >= 0);
        } else return false;
    }

    default void messageDoubleInput(OutputManager om, String context, String constraint) {
        om.print(String.format("Введите %s -%s вещественное число меньше по модулю чем %s", context, constraint, Double.MAX_VALUE));
    }

    default void messageWrongDoubleInput(OutputManager om, String constraint) {
        om.print(String.format("Пожалуйста, введите%s вещественное число меньше по модулю чем %s", constraint, Double.MAX_VALUE));
    }
}

