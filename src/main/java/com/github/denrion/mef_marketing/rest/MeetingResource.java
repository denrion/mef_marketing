package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.Meeting;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.service.MeetingService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("meeting")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MeetingResource {

    @Inject
    MeetingService meetingService;

    @Inject
    PotentialStudentService studentService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(meetingService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        Meeting meeting = meetingService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(meeting)
                .build();
    }

    @POST
    public Response create(@Valid Meeting meeting,
                           @QueryParam("ps_id") Long ps_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        meeting.setPotentialStudent(student);
        meetingService.save(meeting);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(meeting.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(meeting)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid Meeting meeting,
                           @QueryParam("ps_id") Long ps_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        meeting.setPotentialStudent(student);
        Meeting m = meetingService.update(meeting, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(m)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        meetingService.delete(id);

        return Response
                .ok()
                .build();
    }

}
