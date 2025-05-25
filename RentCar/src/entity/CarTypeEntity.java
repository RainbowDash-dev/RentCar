package entity;

import java.math.BigDecimal;

public class CarTypeEntity {

    private long id;
    private String mark;
    private String model;
    private int year;
    private BigDecimal cost;
    private int maxSpeed;

    public CarTypeEntity(long id, String mark, String model,
                         int year, BigDecimal cost, int maxSpeed) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.year = year;
        this.cost = cost;
        this.maxSpeed = maxSpeed;
    }

    public CarTypeEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return "CarTypeEntity{" +
               "availableCarId=" + id +
               ", mark='" + mark + '\'' +
               ", model='" + model + '\'' +
               ", year=" + year +
               ", cost=" + cost +
               ", maxSpeed=" + maxSpeed +
               '}';
    }
}
