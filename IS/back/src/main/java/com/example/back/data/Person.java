package com.example.back.data;

public class Person {
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null
    private String passportID; //Длина строки должна быть не меньше 6, Значение этого поля должно быть уникальным, Поле не может быть null
    private Country nationality; //Поле может быть null
}