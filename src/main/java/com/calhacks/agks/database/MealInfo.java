package com.calhacks.agks.database;

import java.sql.Date;

public class MealInfo {
    private int mealId;
    private String mealName;
    private String foodName;
    private Date date;

    public MealInfo() {}

    public MealInfo(int mealId, String mealName, String foodName, Date date) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.foodName = foodName;
        this.date = date;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
