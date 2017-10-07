package com.calhacks.agks.heroku;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Application config
 */
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Register different Binders
        register(new MainBinder());
    }
}