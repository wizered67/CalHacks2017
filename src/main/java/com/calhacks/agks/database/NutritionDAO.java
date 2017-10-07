package com.calhacks.agks.database;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //User Authentication
    @SqlQuery("SELECT password FROM Users WHERE userName = :username")
    public abstract List<String> userPassword(@Bind("userName") String userName);

    @SqlBatch("INSERT INTO Tokens (userName, token) VALUES (:userName, :token)")
    public abstract void addToken(@Bind("userName") String userName, @Bind("token") String token);

    //Token Authentication
    @SqlQuery("SELECT username FROM tokens WHERE token = :token")
    public abstract List<String> returnUsername(@Bind("token") String token);

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

    @Mapper(DailyNutrientsTargetMapper.class)
    @SqlQuery("SELECT nutrientId, Nutrients.name AS nutrientName, amount " +
            "FROM Nutrients, DailyIntakes, Users " +
            "WHERE Nutrients.id = nutrientId AND Users.userId = :userId AND Users.sex = DailyIntakes.sex " +
            "AND Users.age >= DailyIntakes.lowerAge AND Users.age <= DailyIntakes.upperAge")
    protected abstract List<DailyNutrientsTarget> getDailyNutrientsTargets(@Bind("userId") int userId);

    @Mapper(DailyNutrientsDataMapper.class)
    @SqlQuery("SELECT nutrientId, Nutrients.name AS nutrientName, SUM(amount), Meals.whichDay " +
            "FROM MealNutrition, Meals, Nutrients WHERE Meals.userId = :userId, Meals.id = MealNutrition.mealId AND Nutrients.id = nutrientId" +
            "GROUP BY nutrientId, Meals.whichDay")
    protected abstract List<DailyNutrientsData> getDailyNutrientsData(@Bind("userId") int userId);

    public List<DailyNutritionDifferences> getDailyNutritionDifferences(int userId) {
        /*
        List<DailyNutrientsTarget> dailyNutrientsTargets = getDailyNutrientsTargets(userId);
        List<DailyNutrientsData> dailyNutrientsDatas = getDailyNutrientsData(userId);
        List<DailyNutritionDifferences> dailyNutritionDifferences = new ArrayList<>();
        Map<Date, List<DailyNutritionDifferences>> differencesByDay = new HashMap<>();
        for (DailyNutrientsData dailyData : dailyNutrientsDatas) {
            List<DailyNutritionDifferences> differences = differencesByDay.get(dailyData.getDay());
            if (differences == null) {
                differences = new ArrayList<>();
                differencesByDay.put(dailyData.getDay(), differences);
            }
            differences.add(new DailyNutritionDifferences())
        }
        */
        return null;
    }
}
