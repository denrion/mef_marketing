package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.Meeting;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.security.Auth;
import com.github.denrion.mef_marketing.service.MeetingService;
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

@Path("meeting")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Auth
public class MeetingResource {

    @Inject
    MeetingService meetingService;

    @Inject
    PotentialStudentService studentService;

    @Context
    UriInfo uriInfo;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    @RolesAllowed({"user", "admin"})
    public Response getAll() {
        final List<Meeting> meetings = meetingService.getAll();

        if (meetings == null || meetings.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = meetings.stream()
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
        Meeting meeting = meetingService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(meeting))
                .build();
    }

    @POST
    @RolesAllowed({"user", "admin"})
    public Response create(@Valid Meeting meeting,
                           @QueryParam("ps_id") Long ps_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        meeting.setPotentialStudent(student);

        final Meeting meetingSaved = meetingService.save(meeting);

        return Response
                .status(Response.Status.CREATED)
                .entity(toJson(meetingSaved))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @RolesAllowed({"user", "admin"})
    public Response update(@PathParam("id") Long id,
                           @Valid Meeting meeting,
                           @QueryParam("ps_id") Long ps_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        meeting.setPotentialStudent(student);
        Meeting meetingUpdated = meetingService.update(meeting, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(meetingUpdated))
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        meetingService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(Meeting meeting) {
        URI self = resourceUriBuilder
                .createResourceUri(MeetingResource.class, "getById",
                        meeting.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(MeetingResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("email", meeting.getPotentialStudent().getEmail())
                .add("fullName", meeting.getPotentialStudent().getFullName())
                .add("phone", meeting.getPotentialStudent().getPhone())
                .add("dateDeal", meeting.getDateDeal() != null ? meeting.getDateDeal().toString() : "")
                .add("comment", meeting.getComment())
                .add("visitTime", meeting.getVisitTime())
                .add("whoInvited", meeting.getWhoInvited())
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
