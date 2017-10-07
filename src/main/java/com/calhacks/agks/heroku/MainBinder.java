package com.calhacks.agks.heroku;

import com.calhacks.agks.database.NutritionDAO;
import com.google.common.collect.Sets;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.skife.jdbi.v2.DBI;


import java.util.Set;

public class MainBinder extends AbstractBinder {
    @Override
    protected void configure() {
        String url = "jdbc:mysql://localhost:3306/calhacks";
        String user = "root";
        String password = "root";
        DBI dbi = new DBI(url, user, password);
        NutritionDAO nutritionDAO = dbi.onDemand(NutritionDAO.class);
        bind(nutritionDAO).to(NutritionDAO.class);

        bind("3jgy4WUXKjkhLN2f6p4rC47WE2re7xx0sOed8Xju").named("APIKey").to(String.class);

       // bind(validNutrients).named("Nutrients").to(Set.class);
    }
}
