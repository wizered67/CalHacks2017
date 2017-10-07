package com.calhacks.agks;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        return Response.status(500).build();
    }
}