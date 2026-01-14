package com.example.back.model.entities;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coordinates")
@Cacheable(true)
public class CoordinatesEntity {
    @Id
    private double x;

    @Id
    private Integer y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "CoordinatesEntity{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
