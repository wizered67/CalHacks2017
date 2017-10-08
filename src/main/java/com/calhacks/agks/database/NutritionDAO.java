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
    public static float timeDiff = 60000;

    //User Registration
    @SqlQuery("SELECT username FROM Users WHERE username = :username")
    public abstract List<String> existingAccounts(@Bind("username") String username);

    @SqlUpdate("INSERT INTO Users (username, password, age, sex) VALUES (:username, :password, :age, :sex)")
    public abstract void addUser(@Bind("username") String username, @Bind("password") String password, @Bind("age") int age, @Bind("sex") String sex);

    //User Authentication
    @SqlQuery("SELECT password FROM Users WHERE username = :username")
    public abstract String userPassword(@Bind("username") String username);

    @SqlUpdate("INSERT INTO Tokens (username, token, iter, maxIter, timeVal) VALUES (:username, :token, :iter, :maxIter, :timeVal)")
    public abstract void addToken(@Bind("username") String userName, @Bind("token") String token, @Bind("iter") int iter, @Bind("maxIter") int maxIter, @Bind("timeVal") float timeVal);

    @SqlUpdate("DELETE FROM Tokens WHERE token = :token")
    public abstract void removeToken(@Bind("token") String token);

    //Token Authentication
    @SqlQuery("SELECT username FROM tokens WHERE token = :token")
    public abstract String returnUsername(@Bind("token") String token);

    @SqlQuery("SELECT iter FROM tokens WHERE token = :token")
    public abstract int getIter(@Bind("token") String token);

    @SqlQuery("UPDATE tokens SET iter = :iter WHERE token = :token")
    public abstract int iterate(@Bind("token") String token, @Bind("iter") int iter);

    @SqlQuery("UPDATE tokens SET timeVal = :timeVal WHERE token = :token")
    public abstract float updateTime(@Bind("token") String token, @Bind("timeVal") long timeVal);

    @SqlQuery("SELECT timeVal FROM tokens WHERE token = :token")
    public abstract float checkTime(@Bind("token") String token);



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
