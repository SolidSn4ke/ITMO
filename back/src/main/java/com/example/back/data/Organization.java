package com.example.back.data;

public class Organization {
    private Address officialAddress; //Поле не может быть null
    private Long annualTurnover; //Поле может быть null, Значение поля должно быть больше 0
    private long employeesCount; //Значение поля должно быть больше 0
    private double rating; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
}