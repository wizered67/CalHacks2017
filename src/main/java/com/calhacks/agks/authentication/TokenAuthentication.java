package com.calhacks.agks.authentication;

import com.calhacks.agks.database.NutritionDAO;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthentication implements ContainerRequestFilter {

    java.sql.Timestamp curr = new java.sql.Timestamp(System.currentTimeMillis());
    long currVal = curr.getTime();

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    private NutritionDAO nutritionDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
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
            final String userName = nutritionDAO.returnUsername(token);
            final String userId = nutritionDAO.returnId(userName);
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
            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return userId;
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String s) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });
        }
        catch(Exception e){
            abortWithUnauthorized(requestContext);
        }

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
    }

}


