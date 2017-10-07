package com.calhacks.agks.database;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MealInfoMapper implements ResultSetMapper<MealInfo> {
    @Override
    public MealInfo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new MealInfo(resultSet.getInt("id"), resultSet.getString("mealName"), resultSet.getString("foodName"), resultSet.getDate("whichDay"));
    }
}
