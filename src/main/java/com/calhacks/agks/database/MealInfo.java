package com.calhacks.agks.database;

public class MealInfo {
    private int mealId;
    private String mealName;

    public MealInfo() {}

    public MealInfo(int mealId, String mealName) {
        this.mealId = mealId;
        this.mealName = mealName;
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
}
