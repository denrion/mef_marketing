package com.github.denrion.mef_marketing.config;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final Map<String, String> constraintViolations = new HashMap<>();

        e.getConstraintViolations().forEach(cv ->
                constraintViolations.put(
                        cv.getPropertyPath().toString()
                                .split("\\.")[2] // split because violation pattern -> method.args[num].field
                        , cv.getMessage())
        );

        return Response
                .status(Response.Status.PRECONDITION_FAILED)
                .entity(constraintViolations)
                .build();
    }
}
