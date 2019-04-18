package com.github.denrion.mef_marketing.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException e) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(Collections.singletonMap("error", e.getMessage()))
                .build();
    }
}
