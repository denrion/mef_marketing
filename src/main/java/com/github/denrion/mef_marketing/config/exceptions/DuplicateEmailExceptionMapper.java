package com.github.denrion.mef_marketing.config.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class DuplicateEmailExceptionMapper implements ExceptionMapper<DuplicateEmailException> {

    @Override
    public Response toResponse(DuplicateEmailException e) {
        return Response
                .status(Response.Status.PRECONDITION_FAILED)
                .entity(Collections.singletonMap("email", e.getMessage()))
                .build();
    }
}
