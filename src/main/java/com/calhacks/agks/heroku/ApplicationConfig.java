package com.calhacks.agks.heroku;

import com.calhacks.agks.CustomExceptionMapper;
import com.calhacks.agks.authentication.TokenAuthentication;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Application config
 */
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Register different Binders
        register(new MainBinder());
        register(new JacksonFeature());
        register(CustomExceptionMapper.class);
        register(new TokenAuthentication());
    }
}