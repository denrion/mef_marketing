package com.github.denrion.mef_marketing.config.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final Map<String, String> constraintViolations = new HashMap<>();

        // TODO Find a better way to do this

        Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();

        for (ConstraintViolation<?> cv : cvs) {
            String s = cv.getPropertyPath().toString();

            String[] array = s.split("\\.");

            if (array.length > 2) {
                constraintViolations.put(array[2], cv.getMessage());
            } else if (array.length > 1) {
                constraintViolations.put(array[1], cv.getMessage());
            }
        }

        return Response
                .status(Response.Status.PRECONDITION_FAILED)
                .entity(constraintViolations)
                .build();
    }
}
