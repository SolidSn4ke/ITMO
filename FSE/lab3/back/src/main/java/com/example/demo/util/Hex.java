package com.example.demo.util;

/**
 * Класс для конвертации байтов в их шестнадцатеричное представление
 */
public class Hex {
    /**
     * Конвертация массива байтов в шестнадцатеричную строку
     *
     * @param bytes Массив байтов
     * @return Шестнадцатеричная строка
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
