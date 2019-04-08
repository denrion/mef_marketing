package com.github.denrion.mef_marketing.config;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

    @Override
    public Response toResponse(ClientErrorException e) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(Collections.singletonMap("error", e.getMessage()))
                .build();
    }
}
