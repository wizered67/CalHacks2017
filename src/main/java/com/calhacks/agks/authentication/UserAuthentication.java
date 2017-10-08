package com.calhacks.agks.authentication;

import com.calhacks.agks.database.NutritionDAO;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.mindrot.jbcrypt.BCrypt;
import org.skife.jdbi.org.antlr.runtime.Token;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

//public abstract class UserAuthentication{
//
//    Realm realm = org.apache.shiro.mgt.DefaultSecurityManager;
//    private SecurityManager securityManager = new DefaultSecurityManager(realm);
//
////Make the SecurityManager instance available to the entire application via static memory:
//    SecurityUtils.setSecurityManager(SecurityManager);
//}

@Path("/authentication")
public class UserAuthentication {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response UserInput(@FormParam("username") String userName, @FormParam("password") String password, @Context NutritionDAO nutritionDAO) {
        try {
            //String userName = loginData.getUsername();
            //String password = loginData.getPassword();
            String dbPass = nutritionDAO.userPassword(userName);
            if (!BCrypt.checkpw(password, dbPass)) {
                throw new Exception();
            }
            String token = newToken(userName);
            Timestamp curr = new Timestamp(System.currentTimeMillis());
            long vals = curr.getTime();
            nutritionDAO.addToken(userName, token, 0, nutritionDAO.maxIter, vals);
            return Response.ok().entity(new TokenReceipt(token, nutritionDAO.returnId(userName))).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private static String newToken(String userName) {
        String token = new BigInteger(16, new Random()).toString();
        return token;
    }

    @Secured
    @POST
    @Path("logout")
    public Response logout(@Context SecurityContext securityContext, @Context NutritionDAO nutritionDAO) {
        String userId = securityContext.getUserPrincipal().getName();
        nutritionDAO.removeTokenForUsername(nutritionDAO.getUsernameForUser(userId));
        return Response.seeOther(UriBuilder.fromPath("/index.html").build()).build();
    }

}
