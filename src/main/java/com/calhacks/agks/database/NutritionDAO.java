package com.calhacks.agks.database;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.security.Key;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public final static Key key = MacProvider.generateKey();
    public static int maxIter = 50;

    //User Registration
    @SqlQuery("SELECT username FROM Users WHERE username = :username")
    public abstract List<String> existingAccounts(@Bind("username") String username);

    @SqlBatch("INSERT INTO Users (username, password, age, sex, id) VALUES (:username, password, age, sex)")
    public abstract void addUser(@Bind("username") String username, @Bind("password") String password, @Bind("age") int age, @Bind("sex") String sex);

    //User Authentication
    @SqlQuery("SELECT password FROM Users WHERE username = :username")
    public abstract List<String> userPassword(@Bind("username") String username);

    @SqlBatch("INSERT INTO Tokens (username, token, iter, maxIter, timeVal) VALUES (:username, :token, :iter, :maxIter, :timeVal)")
    public abstract void addToken(@Bind("username") String userName, @Bind("token") String token, @Bind("iter") int iter, @Bind("maxIter") int maxIter, @Bind("timeVal") float timeVal);

    @SqlBatch("DELETE FROM Tokens WHERE username = :username")
    public abstract void removeToken(@Bind("username") String username);

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

    @Mapper(DailyNutrientsDataMapper.class)
    @SqlQuery("SELECT MN.nutrientId, Nutrients.name AS nutrientName, SUM(MN.amount) AS amount, DailyTarget.amount AS target, Meals.whichDay FROM MealNutrition AS MN, Meals, Nutrients, (SELECT nutrientId, Nutrients.name AS nutrientName, amount FROM Nutrients, DailyIntakes, Users WHERE Nutrients.id = DailyIntakes.nutrientId AND Users.id = :userId AND Users.sex = DailyIntakes.sex AND Users.age >= DailyIntakes.lowerAge AND Users.age <= DailyIntakes.upperAge) AS DailyTarget WHERE Meals.userId = :userId AND Meals.id = MN.mealId AND Nutrients.id = MN.nutrientId AND DailyTarget.nutrientId = MN.nutrientId GROUP BY nutrientId, Meals.whichDay")
    protected abstract List<DailyNutrientsData> getDailyNutrientsData(@Bind("userId") int userId);

    public TreeMap<Date, List<DailyNutrientsData>> getDailyNutritionDifferences(int userId) {
        TreeMap<Date, List<DailyNutrientsData>> results = new TreeMap<>(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return -o1.compareTo(o2);
            }
        });
        List<DailyNutrientsData> data = getDailyNutrientsData(userId);
        for (DailyNutrientsData d : data) {
            List<DailyNutrientsData> dayData = results.get(d.getDay());
            if (dayData == null) {
                dayData = new ArrayList<>();
                results.put(d.getDay(), dayData);
            }
            dayData.add(d);
        }
        return results;
    }
}
