package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentPhone;
import com.github.denrion.mef_marketing.service.PotentialStudentPhoneService;

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

@Path("phone")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentPhoneResource {

    @Inject
    PotentialStudentPhoneService psPhoneService;

    @Context
    UriInfo uriInfo;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    public Response getAll() {
        final List<PotentialStudentPhone> students = psPhoneService.getAll();

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
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentPhone student = psPhoneService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(student))
                .build();
    }

    @POST
    public Response create(@Valid PotentialStudentPhone psp) {

        final PotentialStudentPhone student = psPhoneService.save(psp);

        return Response
                .status(Response.Status.CREATED)
                .entity(toJson(student))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentPhone psp) {

        PotentialStudentPhone student = psPhoneService.update(psp, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok()
                .entity(toJson(student))
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        psPhoneService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(PotentialStudentPhone psPhone) {
        URI self = resourceUriBuilder
                .createResourceUri(PotentialStudentPhoneResource.class, "getById",
                        psPhone.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(PotentialStudentPhoneResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("email", psPhone.getPotentialStudent().getEmail())
                .add("fullName", psPhone.getPotentialStudent().getFullName())
                .add("phone", psPhone.getPotentialStudent().getPhone())
                .add("city", psPhone.getCity())
                .add("heardOf", psPhone.getHeardOf())
                .add("studyYear", psPhone.getStudyYear())
                .add("dateVisit", psPhone.getDateVisit() != null ? psPhone.getDateVisit().toString() : "")
                .add("price", psPhone.getPrice())
                .add("whoCalledMef", psPhone.getWhoCalledMef())
                .add("enrollment", psPhone.getEnrollment())
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
