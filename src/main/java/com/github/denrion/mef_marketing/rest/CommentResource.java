package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AdminUser;
import com.github.denrion.mef_marketing.entity.Comment;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.service.AdminUserService;
import com.github.denrion.mef_marketing.service.CommentService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {


    @Inject
    CommentService commentService;

    @Inject
    AdminUserService userService;

    @Inject
    PotentialStudentService studentService;


    @Context
    private UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(commentService.getAll())
                .build();
    }


    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        Comment comment = commentService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(comment)
                .build();
    }

    @GET
    @Path("student/{ps_id: \\d+}")
    public Response getAllByPSId(@PathParam("ps_id") Long ps_id) {
        return Response
                .ok(commentService.getAllByPSId(ps_id))
                .build();
    }

    @POST
    public Response create(@Valid Comment comm,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        AdminUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        comm.setPotentialStudent(student);
        comm.setUser(user);

        Comment comment = commentService.save(comm);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(comment.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(comment)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid Comment comm,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        AdminUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        comm.setPotentialStudent(student);
        comm.setUser(user);

        Comment comment = commentService.update(comm, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(comment)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        commentService.delete(id);

        return Response
                .ok()
                .build();
    }
}
