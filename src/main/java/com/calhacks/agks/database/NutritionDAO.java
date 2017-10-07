package com.calhacks.agks.database;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.List;

public abstract class NutritionDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Meals (userId, name) VALUES (:userId, :name)")
    protected abstract int createMeal(@Bind("userId") int userId, @Bind("name") String name);

    @SqlBatch("INSERT INTO MealNutrition (mealId, nutrientId, amount) VALUES (:mealId, :nutrientId, :amount)")
    protected abstract void createMealNutrition(@Bind("mealId") int mealId,
                                                @Bind("nutrientId") List<Integer> nutrientId, @Bind("amount") List<Float> amount);

    public void addMeal(int userId, String name, List<Integer> nutrientIds, List<Float> nutrientAmounts) {
        int id = createMeal(userId, name);
        createMealNutrition(id, nutrientIds, nutrientAmounts);
    }
}
