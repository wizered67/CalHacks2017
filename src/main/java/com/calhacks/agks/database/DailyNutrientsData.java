package com.calhacks.agks.database;

import java.sql.Date;

public class DailyNutrientsData {
    private int nutrientId;
    private String nutrientName;
    private float dailyTotal;
    private Date day;

    public DailyNutrientsData() {}

    public DailyNutrientsData(int nutrientId, String nutrientName, float dailyTotal, Date day) {
        this.nutrientId = nutrientId;
        this.nutrientName = nutrientName;
        this.dailyTotal = dailyTotal;
        this.day = day;
    }

    public int getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(int nutrientId) {
        this.nutrientId = nutrientId;
    }

    public float getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(float dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }
}
