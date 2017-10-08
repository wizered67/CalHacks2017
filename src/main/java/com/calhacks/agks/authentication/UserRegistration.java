package com.calhacks.agks.authentication;

import com.calhacks.agks.database.NutritionDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.skife.jdbi.org.antlr.runtime.Token;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Sabrina on 10/7/2017.
 */
@Path("registration")
public class UserRegistration {
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public Response userInput(@FormParam("username") String username, @FormParam("password") String password, @FormParam("age") int age, @FormParam("sex") String sex, @Context NutritionDAO nutritionDAO) {
        List<String> otherAccounts = nutritionDAO.existingAccounts(username);
        Timestamp curr = new Timestamp(System.currentTimeMillis());
        long vals = curr.getTime();
        if (otherAccounts.size() > 1) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        String newPassword = Jwts.builder().setSubject(password).signWith(SignatureAlgorithm.HS256, nutritionDAO.key).compact();
        nutritionDAO.addUser(username, newPassword, age, sex);
        String token = newToken(username);
        nutritionDAO.addToken(username, token, 0, nutritionDAO.maxIter, vals);
        return Response.ok(token).build();
    }

    private String newToken(String userName) {
        String token = Token.EOF_TOKEN.toString();
        return token;
    }

}