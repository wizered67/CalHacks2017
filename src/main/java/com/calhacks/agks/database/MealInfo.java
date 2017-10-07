package com.calhacks.agks.database;

import java.sql.Date;

public class MealInfo {
    private int mealId;
    private String mealName;
    private Date date;

    public MealInfo() {}

    public MealInfo(int mealId, String mealName, Date date) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.date = date;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
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
