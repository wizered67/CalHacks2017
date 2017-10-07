package com.calhacks.agks;

import com.calhacks.agks.database.NutritionDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("users/")
public class UsersResource {
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/food")
    @GET
    public Response getAllFood(@Context NutritionDAO nutritionDAO, @PathParam("id") int id) {
        return Response.ok().entity(nutritionDAO.getAllMealsAndNutritionInfo(id)).build();
    }

    @Path("{id}/food")
    @POST
    public Response addFood(@Context @Named("APIKey") String apiKey, @Context NutritionDAO nutritionDAO, @PathParam("id") int id, @QueryParam("name") String name, @QueryParam("NBDno") String NBDno) {
        Client c = ClientBuilder.newClient();
        String responseString = c.target("https://api.nal.usda.gov/ndb/V2/reports?ndbno=" + NBDno + "&type=b&format=json&api_key=" + apiKey).request().get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(responseString);
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

            nutritionDAO.addMeal(id, name, nutrientIds, nutrientContent);
            return Response.ok().build();
        } catch (IOException io) {

        }
        return Response.noContent().build();
    }
}
