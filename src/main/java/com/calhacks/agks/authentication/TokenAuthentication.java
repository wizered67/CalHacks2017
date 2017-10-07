package com.calhacks.agks.authentication;

import com.calhacks.agks.database.NutritionDAO;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public abstract class TokenAuthentication {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    public void filter(ContainerRequestContext requestContext, @Context NutritionDAO nutritionDAO) throws IOException {
        String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            UserAuthentication.UserInput("username", "password", nutritionDAO);
            return;
        }
        if (!header.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ")){
            abortWithUnauthorized(requestContext);
            return;
        }
        try{
            String token = header.subSequence(7, header.length()).toString();
            List<String> userNames = nutritionDAO.returnUsername(token);
            validateToken(userNames);
        }
        catch(Exception e){
            abortWithUnauthorized(requestContext);
        }

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
    }

    private void validateToken(List<String> userNames) throws Exception{
        if (userNames.size() != 1) {
            throw new Exception();
        }
        //add something for when it expires
    }

    public String findUsername(List<String> userNames){
        if (userNames.size() != 1) {
            return userNames.get(0);
        } else return null;
    }
}

