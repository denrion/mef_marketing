package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.service.AppUserService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class AppUserResource {

    @Inject
    AppUserService userService;

    @Context
    private UriInfo uriInfo;

    @Inject
    private ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    public Response getAll() {
        List<AppUser> users = userService.getAll();

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
        AppUser user = userService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(user))
                .build();
    }

    @POST
    public Response create(@Valid AppUser user) {
        AppUser appUser = userService.save(user);

        return Response
                .ok()
                .status(Response.Status.CREATED)
                .entity(toJson(appUser))
                .build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@NotBlank @FormParam("username") String username,
                          @NotBlank @FormParam("password") String password) {

        boolean authenticated = userService.authenticateUser(username, password);

        if (!authenticated) {
            throw new SecurityException("Email or password not valid");
        }

        return Response
                .ok()
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid AppUser user) {

        AppUser appUser = userService.update(user, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok()
                .entity(toJson(appUser))
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

    private JsonObject toJson(AppUser user) {
        URI self = resourceUriBuilder
                .createResourceUri(AppUserResource.class, "getById",
                        user.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(AppUserResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("username", user.getUsername())
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
