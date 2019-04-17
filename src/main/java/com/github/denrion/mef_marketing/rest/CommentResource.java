package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.entity.Comment;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.service.AppUserService;
import com.github.denrion.mef_marketing.service.CommentService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

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

@Path("comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    CommentService commentService;

    @Inject
    AppUserService userService;

    @Inject
    PotentialStudentService studentService;

    @Context
    private UriInfo uriInfo;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @GET
    @Path("")
    public Response getAll() {
        final List<Comment> comments = commentService.getAll();

        if (comments == null || comments.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = comments.stream()
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
        Comment comment = commentService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(comment))
                .build();
    }

    @GET
    @Path("student/{ps_id: \\d+}")
    public Response getAllByPSId(@PathParam("ps_id") Long ps_id) {
        final List<Comment> comments = commentService.getAllByPSId(ps_id);

        if (comments == null || comments.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = comments.stream()
                .map(this::toJson)
                .collect(JsonCollectors.toJsonArray());

        return Response
                .ok()
                .entity(data)
                .build();
    }

    @POST
    public Response create(@Valid Comment comm,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        AppUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        comm.setPotentialStudent(student);
        comm.setUser(user);

        Comment comment = commentService.save(comm);

        return Response
                .status(Response.Status.CREATED)
                .entity(toJson(comment))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid Comment comm,
                           @QueryParam("ps_id") Long ps_id,
                           @QueryParam("user_id") Long user_id) {

        AppUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        comm.setPotentialStudent(student);
        comm.setUser(user);

        Comment comment = commentService.update(comm, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(toJson(comment))
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

    private JsonObject toJson(Comment comment) {
        URI self = resourceUriBuilder
                .createResourceUri(CommentResource.class, "getById",
                        comment.getId(), uriInfo);

        URI others = resourceUriBuilder
                .createResourceUri(CommentResource.class, "getAll",
                        uriInfo);

        return Json.createObjectBuilder()
                .add("email", comment.getPotentialStudent().getEmail())
                .add("studentFullName", comment.getPotentialStudent().getFullName())
                .add("comment", comment.getComment())
                .add("userFullName", comment.getUser().getFullName())
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
