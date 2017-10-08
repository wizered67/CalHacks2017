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
import java.security.Timestamp;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public abstract class TokenAuthentication {

    java.sql.Timestamp curr = new java.sql.Timestamp(System.currentTimeMillis());
    long currVal = curr.getTime();

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    public void filter(ContainerRequestContext requestContext, @Context NutritionDAO nutritionDAO) throws IOException {
        String header = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            abortWithUnauthorized(requestContext);
            return;
        }
        if (!header.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ")){
            abortWithUnauthorized(requestContext);
            return;
        }
        try{
            String token = header.subSequence(7, header.length()).toString();
            String userNames = nutritionDAO.returnUsername(token);
            validateToken(userNames);
            int temp = nutritionDAO.getIter(token);
            if (temp > nutritionDAO.maxIter) {
                nutritionDAO.removeToken(token);
                throw new Exception();
            }
            if (nutritionDAO.checkTime(token) + nutritionDAO.timeDiff < currVal) {
                throw new Exception();
            }
            nutritionDAO.iterate(token, temp + 1);
            nutritionDAO.updateTime(token, currVal);
        }
        catch(Exception e){
            abortWithUnauthorized(requestContext);
        }

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
    }

    private void validateToken(String userNames) throws Exception{

        //add something for when it expires
    }

    public String findUsername(List<String> userNames){
        if (userNames.size() != 1) {
            return userNames.get(0);
        } else return null;
    }
}


