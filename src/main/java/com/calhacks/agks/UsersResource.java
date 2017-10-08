package com.calhacks.agks;

import com.calhacks.agks.authentication.Secured;
import com.calhacks.agks.database.DailyNutrientsData;
import com.calhacks.agks.database.MealPost;
import com.calhacks.agks.database.NutritionDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Path("users/")
public class UsersResource {
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/food")
    @GET
    public Response getAllFood(@Context NutritionDAO nutritionDAO, @Context SecurityContext securityContext, @PathParam("id") int id) {
        checkSameUser(securityContext, id);
        return Response.ok().entity(nutritionDAO.getAllMealsAndNutritionInfo(id)).build();
    }

    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/food")
    @POST
    public Response addFood(@Context @Named("APIKey") String apiKey, @Context NutritionDAO nutritionDAO, @Context SecurityContext securityContext, @PathParam("id") int id, MealPost mealPost) {
        checkSameUser(securityContext, id);
        Client c = ClientBuilder.newClient();
        String responseString = c.target("https://api.nal.usda.gov/ndb/V2/reports?ndbno=" + mealPost.getNBDno() + "&type=b&format=json&api_key=" + apiKey).request().get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(responseString);
            JsonNode descNode = node.findValue("desc");
            String foodName = descNode.get("name").asText();
            JsonNode nutrientsNode = node.findValue("nutrients");
            Set<Integer> validNutrients = Sets.newHashSet(301, 203, 320, 401, 324, 323);
            List<Integer> nutrientIds = new ArrayList<>();
            List<Float> nutrientContent = new ArrayList<>();

            for (int i = 0; i < nutrientsNode.size(); i += 1) {
                JsonNode nutrientInfo = nutrientsNode.get(i);
                int nutrientId = nutrientInfo.get("nutrient_id").asInt();
                if (validNutrients.contains(nutrientId)) {
                    nutrientIds.add(nutrientId);
                    nutrientContent.add((float)nutrientInfo.get("value").asDouble());
                }
            }

            nutritionDAO.addMeal(id, mealPost.getName(), foodName, mealPost.getDate(), nutrientIds, nutrientContent);
            return Response.ok().build();
        } catch (IOException io) {

        }
        return Response.noContent().build();
    }

    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/recommendations")
    @GET
    public Response getNutritionInfo(@Context NutritionDAO nutritionDAO, @Context SecurityContext securityContext, @PathParam("id") int id) {
        checkSameUser(securityContext, id);
        TreeMap<Date, List<DailyNutrientsData>> data = nutritionDAO.getDailyNutritionDifferences(id);
        int i = 0;
        TreeMap<Integer, List<Float>> totalCalculations = new TreeMap<>();

        for (List<DailyNutrientsData> dailyData : data.values()) {
            for (DailyNutrientsData nutrientData : dailyData) {
                List<Float> totals = totalCalculations.get(nutrientData.getNutrientId());
                if (totals == null) {
                    totals = Lists.newArrayList(0f, 0f);
                    totalCalculations.put(nutrientData.getNutrientId(), totals);
                }
                totals.set(0, totals.get(0) + nutrientData.getDailyTotal());
                totals.set(1, totals.get(1) + nutrientData.getDailyTarget());
            }
            i += 1;
            if (i >= 3) {
                break;
            }
        }

        return Response.ok().build();
    }

    private void checkSameUser(SecurityContext securityContext, int id) throws NotAuthorizedException {
        if (!Integer.toString(id).equals(securityContext.getUserPrincipal().getName())) {
            throw new NotAuthorizedException("A user can only access their own data.");
        }
    }
}
