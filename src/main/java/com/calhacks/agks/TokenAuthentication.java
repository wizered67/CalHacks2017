package com.calhacks.agks;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import javax.annotation.Priority;
import javax.ws.rs.NameBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public abstract class TokenAuthentication {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

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
            validateToken(header);
        }
        catch(Exception e){
            abortWithUnauthorized(requestContext);
        }

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
    }

    @SqlQuery("SELECT username FROM tokens WHERE token = :token")
    protected abstract List<String> returnUsername(@Bind("token") String token);
    private void validateToken(String token) throws Exception{
        List<String> userNames = returnUsername(token);
        if (userNames.size() != 1) {
            throw new Exception();
        }
        //add something for when it expires
    }

    public String findUsername(String token){
        List<String> userNames = returnUsername(token);
        if (userNames.size() != 1) {
            return userNames.get(0);
        } else return null;
    }
}

