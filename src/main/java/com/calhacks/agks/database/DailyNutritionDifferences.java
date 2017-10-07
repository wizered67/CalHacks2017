package com.calhacks.agks.database;

import java.sql.Date;
import java.util.List;

public class DailyNutritionDifferences {
    private Date day;
    private List<Integer> nutrientIds;
    private List<String> nutrientNames;
    private List<Float> nutrientDifferences;

    public DailyNutritionDifferences(Date day, List<Integer> nutrientIds, List<String> nutrientNames, List<Float> nutrientDifferences) {
        this.day = day;
        this.nutrientIds = nutrientIds;
        this.nutrientNames = nutrientNames;
        this.nutrientDifferences = nutrientDifferences;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<Integer> getNutrientIds() {
        return nutrientIds;
    }

    public void setNutrientIds(List<Integer> nutrientIds) {
        this.nutrientIds = nutrientIds;
    }

    public List<String> getNutrientNames() {
        return nutrientNames;
    }

    public void setNutrientNames(List<String> nutrientNames) {
        this.nutrientNames = nutrientNames;
    }

    public List<Float> getNutrientDifferences() {
        return nutrientDifferences;
    }

    public void setNutrientDifferences(List<Float> nutrientDifferences) {
        this.nutrientDifferences = nutrientDifferences;
    }
}
