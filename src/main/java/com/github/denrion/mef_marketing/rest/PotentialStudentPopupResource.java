package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentPopup;
import com.github.denrion.mef_marketing.security.Auth;
import com.github.denrion.mef_marketing.service.PotentialStudentPopupService;

import javax.annotation.security.RolesAllowed;
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

@Path("popup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Auth
public class PotentialStudentPopupResource {

    @Inject
    PotentialStudentPopupService psPopupService;

    @Context
    UriInfo uriInfo;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    @RolesAllowed({"user", "admin"})
    public Response getAll() {
        final List<PotentialStudentPopup> students = psPopupService.getAll();

        if (students == null || students.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = students.stream()
                .map(this::toJson)
                .collect(JsonCollectors.toJsonArray());

        return Response
                .ok()
                .entity(data)
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    @RolesAllowed({"user", "admin"})
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentPopup student = psPopupService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(student))
                .build();
    }

    @POST
    @RolesAllowed({"user", "admin"})
    public Response create(@Valid PotentialStudentPopup psPopup) {

        final PotentialStudentPopup student = psPopupService.save(psPopup);

        return Response
                .status(Response.Status.CREATED)
                .entity(toJson(student))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @RolesAllowed({"user", "admin"})
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentPopup psPopup) {

        PotentialStudentPopup student = psPopupService.update(psPopup, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(student))
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        psPopupService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(PotentialStudentPopup psPopup) {
        URI self = resourceUriBuilder
                .createResourceUri(PotentialStudentPopupResource.class, "getById",
                        psPopup.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(PotentialStudentPopupResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("email", psPopup.getPotentialStudent().getEmail())
                .add("fullName", psPopup.getPotentialStudent().getFullName())
                .add("phone", psPopup.getPotentialStudent().getPhone())
                .add("dateContact", psPopup.getDateContact() != null ? psPopup.getDateContact().toString() : "")
                .add("dateSignUp", psPopup.getDateSignUp() != null ? psPopup.getDateSignUp().toString() : "")
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
