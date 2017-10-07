package com.calhacks.agks.database;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class MealAndNutritionInfo {
    private String name;
    private int mealId;
    private List<NutrientInfo> nutrients;

    public MealAndNutritionInfo() {}
    public MealAndNutritionInfo(String name, int mealId, List<NutrientInfo> nutrients) {
        this.name = name;
        this.mealId = mealId;
        this.nutrients = nutrients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public List<NutrientInfo> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<NutrientInfo> nutrients) {
        this.nutrients = nutrients;
    }
}
