package com.calhacks.agks;

import org.apache.shiro.SecurityUtils;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    public Response UserInput(@FormParam("username") String userName, @FormParam("password") String password) {
        try {
            authenticate(userName, password);
            String token = newToken(userName);
            return Response.ok(token).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @SqlQuery("SELECT password FROM Users WHERE userName = :username")
    protected abstract List<String> userPassword(@Bind("userName") String userName);
    private void authenticate(String userName, String givenPassword) throws Exception{
        List<String> passwordList = userPassword(userName);
        if (passwordList.size() > 1) {
            throw new Exception();
        }
        if (passwordList.get(0) != givenPassword) {
            throw new Exception();
        }
    }

    @SqlBatch("INSERT INTO Tokens (userName, token) VALUES (:userName, :token)")
    protected abstract void addToken(@Bind("userName") String userName, @Bind("token") String token);
    private String newToken(String userName) {
        String token = Token.EOF_TOKEN.toString();
        addToken(userName, token);
        return token;
    }
}
