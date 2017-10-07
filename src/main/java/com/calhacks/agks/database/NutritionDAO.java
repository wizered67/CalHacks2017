package com.calhacks.agks.database;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class NutritionDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Meals (userId, mealName, foodName, whichDay) VALUES (:userId, :mealName, :foodName, :date)")
    protected abstract int createMeal(@Bind("userId") int userId, @Bind("mealName") String name, @Bind("foodName") String foodName, @Bind("date") String date);

    @SqlBatch("INSERT INTO MealNutrition (mealId, nutrientId, amount) VALUES (:mealId, :nutrientId, :amount)")
    protected abstract void createMealNutrition(@Bind("mealId") int mealId,
                                                @Bind("nutrientId") List<Integer> nutrientId, @Bind("amount") List<Float> amount);

    public void addMeal(int userId, String mealName, String foodName, String date, List<Integer> nutrientIds, List<Float> nutrientAmounts) {
        int id = createMeal(userId, mealName, foodName, date);
        createMealNutrition(id, nutrientIds, nutrientAmounts);
    }

    @Mapper(MealInfoMapper.class)
    @SqlQuery("SELECT id, mealName, foodName, whichDay FROM Meals WHERE userId = :userId ORDER BY whichDay DESC")
    protected abstract List<MealInfo> getAllMealInfo(@Bind("userId") int userId);

    @Mapper(NutrientInfoMapper.class)
    @SqlQuery("SELECT nutrientId, Nutrients.name, amount FROM Nutrients, MealNutrition WHERE mealId = :mealId AND nutrientId = Nutrients.id")
    protected abstract List<NutrientInfo> getAllNutrientInfo(@Bind("mealId") int mealId);

    public List<MealAndNutritionInfo> getAllMealsAndNutritionInfo(int userId) {
        List<MealAndNutritionInfo> allResults = new ArrayList<>();
        List<MealInfo> allMealsInfo = getAllMealInfo(userId);
        for (MealInfo mealInfo : allMealsInfo) {
            List<NutrientInfo> nutrientsInfo = getAllNutrientInfo(mealInfo.getMealId());
            allResults.add(new MealAndNutritionInfo(mealInfo, nutrientsInfo));
        }
        return allResults;
    }
}
