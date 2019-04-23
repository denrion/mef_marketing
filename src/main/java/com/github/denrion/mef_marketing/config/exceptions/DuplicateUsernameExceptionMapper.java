package com.github.denrion.mef_marketing.config.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class DuplicateUsernameExceptionMapper implements ExceptionMapper<DuplicateUsernameException> {

    @Override
    public Response toResponse(DuplicateUsernameException e) {
        return Response
                .status(Response.Status.PRECONDITION_FAILED)
                .entity(Collections.singletonMap("username", e.getMessage()))
                .build();
    }
}