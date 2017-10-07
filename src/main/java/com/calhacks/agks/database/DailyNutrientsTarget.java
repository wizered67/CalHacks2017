package com.calhacks.agks.database;

public class DailyNutrientsTarget {
    private int nutrientId;
    private String nutrientName;
    private float amount;

    public DailyNutrientsTarget(int nutrientId, String nutrientName, float amount) {
        this.nutrientId = nutrientId;
        this.nutrientName = nutrientName;
        this.amount = amount;
    }

    public int getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(int nutrientId) {
        this.nutrientId = nutrientId;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
