package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AdminUser;
import com.github.denrion.mef_marketing.service.AdminUserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminUserResource {

    @Inject
    AdminUserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getAllUsers() {
        return Response
                .ok(userService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getUserById(@PathParam("id") Long id) {
        AdminUser user = userService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(user)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createUser(@BeanParam @Valid AdminUser user) {
        AdminUser adminUser = userService.save(user);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(adminUser.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(adminUser)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUser(@PathParam("id") Long id, @BeanParam @Valid AdminUser user) {
        AdminUser adminUser = userService.update(user, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(adminUser)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.delete(id);

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
