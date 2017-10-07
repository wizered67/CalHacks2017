package com.calhacks.agks;

import com.calhacks.agks.database.NutritionDAO;
import com.google.common.collect.Sets;

import javax.inject.Named;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("users/")
public class UsersResource {
    @Path("{id}/food")
    @POST
    public Response addFood(@Context @Named("APIKey") String apiKey, @Context NutritionDAO nutritionDAO, @PathParam("id") int id, @QueryParam("name") String name, @QueryParam("NBDno") String NBDno) {
        Client c = ClientBuilder.newClient();
        JsonObject response = c.target("https://api.nal.usda.gov/ndb/V2/reports?ndbno=" + NBDno + "&type=b&format=json&api_key=" + apiKey).request().get(JsonObject.class);
        JsonArray array = response.get("foods").asJsonArray().get(0).asJsonObject().get("food").asJsonObject().get("nutrients").asJsonArray();
        Set<Integer> validNutrients = Sets.newHashSet(301, 203, 320, 401, 324, 323);
        List<Integer> nutrientIds = new ArrayList<>();
        List<Float> nutrientContent = new ArrayList<>();

        for (int i = 0; i < array.size(); i += 1) {
            JsonObject nutritionInfo = array.get(i).asJsonObject();
            int nutrientId = Integer.parseInt(nutritionInfo.get("nutrient_id").toString().replace("\"", ""));
            if (validNutrients.contains(nutrientId)) {
                nutrientIds.add(nutrientId);
                nutrientContent.add(Float.parseFloat(nutritionInfo.get("value").toString().replace("\"", "")));
            }
        }
        nutritionDAO.addMeal(id, name, nutrientIds, nutrientContent);
        return Response.ok().build();
    }
}
