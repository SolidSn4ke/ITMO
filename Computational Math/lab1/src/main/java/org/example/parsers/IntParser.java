package org.example.parsers;

import org.example.system.io.InputManager;
import org.example.system.io.OutputManager;

import java.math.BigInteger;

/**
 * Интерфейс для определения пользовательского ввода для целых чисел типа int/Integer
 */
public interface IntParser {
    default Integer parse(InputManager source, String context, Boolean mayBeNull, Boolean mayBeLessThan1, Integer maxValue) {
        OutputManager outputManager = new OutputManager();

        String constraint;

        if (mayBeNull) {
            context = context + " (введите \"skip\", чтобы пропустить)";
        }
        if (mayBeLessThan1) {
            constraint = "целое";
            messageIntInput(outputManager, context, constraint, Integer.MIN_VALUE, maxValue);
        } else {
            constraint = "натуральное";
            messageIntInput(outputManager, context, constraint, 1, maxValue);
        }

        String input;
        String regex;
        if (mayBeLessThan1) {
            regex = "(?:-?[1-9]\\d*|0)";
        } else {
            regex = "[1-9]\\d*";
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
                if (input.matches(regex)
                        && (new BigInteger(input).abs().compareTo(new BigInteger("2147483648"))) < 0
                        && Integer.parseInt(input) <= maxValue) {
                    return Integer.parseInt(input);
                } else {
                    messageWrongIntInput(outputManager, constraint, maxValue);
                    outputManager.messageInputPrompt();
                }
            }
        }
    }

    default void messageIntInput(OutputManager om, String context, String constraint, Integer minValue, Integer maxValue) {
        om.print(String.format("Введите %s - %s число от %d до %d", context, constraint, minValue, maxValue));
    }

    default void messageWrongIntInput(OutputManager om, String constraint, Integer maxValue) {
        om.print(String.format("Пожалуйста, введите %s число меньше %d", constraint, maxValue));
    }
}
