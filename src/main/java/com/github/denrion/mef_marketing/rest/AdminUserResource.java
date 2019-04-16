package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AdminUser;
import com.github.denrion.mef_marketing.service.AdminUserService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminUserResource {

    @Inject
    AdminUserService userService;

    @Context
    private UriInfo uriInfo;

    @Inject
    private ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    public Response getAll() {
        List<AdminUser> users = userService.getAll();

        if (users == null || users.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = users.stream()
                .map(this::toJson)
                .collect(JsonCollectors.toJsonArray());

        return Response
                .ok()
                .entity(data)
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        AdminUser user = userService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(user))
                .build();
    }

    @POST
    public Response create(@Valid AdminUser user) {
        AdminUser adminUser = userService.save(user);

        if (adminUser == null) {
            return Response
                    .noContent()
                    .build();
        }

        return Response
                .ok()
                .status(Response.Status.CREATED)
                .entity(toJson(adminUser))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid AdminUser user) {

        AdminUser adminUser = userService.update(user, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok()
                .entity(toJson(adminUser))
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(AdminUser user) {
        URI self = resourceUriBuilder
                .createResourceUri(AdminUserResource.class, "getById",
                        user.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(AdminUserResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("fullName", user.getFullName())
                .add("_links", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("rel", "self")
                                .add("href", self.toString()))
                        .add(Json.createObjectBuilder()
                                .add("rel", "all")
                                .add("href", others.toString())
                        )
                ).build();
    }
}
