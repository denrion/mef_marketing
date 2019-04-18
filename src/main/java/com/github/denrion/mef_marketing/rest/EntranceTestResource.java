package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.entity.EntranceTest;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.security.Auth;
import com.github.denrion.mef_marketing.service.AppUserService;
import com.github.denrion.mef_marketing.service.EntranceTestService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

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

@Path("tests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Auth
public class EntranceTestResource {

    @Inject
    EntranceTestService testService;

    @Inject
    PotentialStudentService studentService;

    @Inject
    AppUserService userService;

    @Context
    UriInfo uriInfo;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    @RolesAllowed({"user", "admin"})
    public Response getAll() {
        final List<EntranceTest> tests = testService.getAll();

        if (tests == null || tests.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = tests.stream()
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
        EntranceTest test = testService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(test))
                .build();
    }

    @POST
    @RolesAllowed({"user", "admin"})
    public Response create(@Valid EntranceTest test,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        AppUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        test.setUser(user);
        test.setPotentialStudent(student);

        EntranceTest entranceTest = testService.save(test);

        return Response
                .status(Response.Status.CREATED)
                .entity(toJson(entranceTest))
                .build();

    }

    @PUT
    @Path("{id: \\d+}")
    @RolesAllowed({"user", "admin"})
    public Response update(@PathParam("id") Long id,
                           @Valid EntranceTest test,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        AppUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        test.setUser(user);
        test.setPotentialStudent(student);

        EntranceTest entranceTest = testService.update(test, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(entranceTest))
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        testService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(EntranceTest test) {
        URI self = resourceUriBuilder
                .createResourceUri(EntranceTestResource.class, "getById",
                        test.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(EntranceTestResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("email", test.getPotentialStudent().getEmail())
                .add("studentFullName", test.getPotentialStudent().getFullName())
                .add("phone", test.getPotentialStudent().getPhone())
                .add("dateDeal", test.getDateDeal() != null ? test.getDateDeal().toString() : "")
                .add("dateMailSent", test.getDateMailSent() != null ? test.getDateMailSent().toString() : "")
                .add("mailContent", test.getMailContent())
                .add("comment", test.getComment())
                .add("userFullName", test.getUser().getFullName())
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
