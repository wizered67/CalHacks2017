package com.calhacks.agks.database;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyNutrientsDataMapper implements ResultSetMapper<DailyNutrientsData> {
    @Override
    public DailyNutrientsData map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new DailyNutrientsData(resultSet.getInt("nutrientId"), resultSet.getString("nutrientName"), resultSet.getFloat("amount"), resultSet.getDate("whichDay"));
    }
}
