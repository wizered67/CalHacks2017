package com.calhacks.agks.database;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NutrientInfoMapper implements ResultSetMapper<NutrientInfo> {
    @Override
    public NutrientInfo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new NutrientInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3));
    }
}
