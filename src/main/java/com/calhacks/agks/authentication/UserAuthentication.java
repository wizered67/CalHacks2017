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
import org.skife.jdbi.org.antlr.runtime.Token;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import static org.apache.shiro.SecurityUtils.securityManager;

//public abstract class UserAuthentication{
//
//    Realm realm = org.apache.shiro.mgt.DefaultSecurityManager;
//    private SecurityManager securityManager = new DefaultSecurityManager(realm);
//
////Make the SecurityManager instance available to the entire application via static memory:
//    SecurityUtils.setSecurityManager(SecurityManager);
//}

@Path("/authentication")
public abstract class UserAuthentication {

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response UserInput(@FormParam("username") String userName, @FormParam("password") String password, @Context NutritionDAO nutritionDAO) {
        try {
            List<String> passwordList = nutritionDAO.userPassword(userName);
            String newPassword = Jwts.builder().setSubject(password).signWith(SignatureAlgorithm.HS256, nutritionDAO.key).compact();
            authenticate(passwordList, newPassword);
            String token = newToken(userName);
            Timestamp curr = new Timestamp(System.currentTimeMillis());
            long vals = curr.getTime();
            nutritionDAO.addToken(userName, token, 0, nutritionDAO.maxIter, vals);
            return Response.ok(token).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private static void authenticate(List<String> passwordList, String newPassword) throws Exception{
        if (passwordList.size() > 1) {
            throw new Exception();
        }

        if (passwordList.get(0) != newPassword) {
            throw new Exception();
        }
    }

    private static String newToken(String userName) {
        String token = Token.EOF_TOKEN.toString();
        return token;
    }
}
