package com.calhacks.agks.database;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class MealAndNutritionInfo {
    private MealInfo mealInfo;
    private List<NutrientInfo> nutrients;

    public MealAndNutritionInfo() {}
    public MealAndNutritionInfo(MealInfo mealInfo, List<NutrientInfo> nutrients) {
        this.mealInfo = mealInfo;
        this.nutrients = nutrients;
    }

    public MealInfo getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(MealInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    public List<NutrientInfo> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<NutrientInfo> nutrients) {
        this.nutrients = nutrients;
    }
}
