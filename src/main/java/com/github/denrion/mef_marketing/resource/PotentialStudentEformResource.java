//package com.github.denrion.mef_marketing.resource;
//
//import com.github.denrion.mef_marketing.entity.PotentialStudentEform;
//import com.github.denrion.mef_marketing.config.security.Auth;
//import com.github.denrion.mef_marketing.service.PotentialStudentEformService;
//import org.eclipse.microprofile.openapi.annotations.tags.Tag;
//
//import javax.annotation.security.RolesAllowed;
//import javax.inject.Inject;
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.stream.JsonCollectors;
//import javax.validation.Valid;
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriInfo;
//import java.net.URI;
//import java.util.List;
//
//@Tag(name = "PotentialStudentEformResource",
//        description = "CRUD for potential students who contacted through electronic form")
//@Path("eform")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
//@Auth
//public class PotentialStudentEformResource {
//
//    @Inject
//    PotentialStudentEformService psEformService;
//
//    @Context
//    UriInfo uriInfo;
//
//    @Inject
//    ResourceUriBuilder resourceUriBuilder;
//
//    @GET
//    @Path("")
//    @RolesAllowed({"user", "admin"})
//    public Response getAll() {
//        final List<PotentialStudentEform> students = psEformService.getAll();
//
//        if (students == null || students.isEmpty()) {
//            return Response
//                    .noContent()
//                    .build();
//        }
//
//        JsonArray data = students.stream()
//                .map(this::addHypermediaLinks)
//                .collect(JsonCollectors.toJsonArray());
//
//        return Response
//                .ok()
//                .entity(data)
//                .build();
//    }
//
//    @GET
//    @Path("{id: \\d+}")
//    @RolesAllowed({"user", "admin"})
//    public Response getById(@PathParam("id") Long id) {
//        PotentialStudentEform student = psEformService.getById(id)
//                .orElseThrow(NotFoundException::new);
//
//        return Response
//                .ok(addHypermediaLinks(student))
//                .build();
//    }
//
//    @POST
//    @RolesAllowed({"user", "admin"})
//    public Response create(@Valid PotentialStudentEform pse) {
//
//        final PotentialStudentEform student = psEformService.save(pse);
//
//        return Response
//                .status(Response.Status.CREATED)
//                .entity(addHypermediaLinks(student))
//                .build();
//    }
//
//    @PUT
//    @Path("{id: \\d+}")
//    public Response update(@PathParam("id") Long id,
//                           @Valid PotentialStudentEform pse) {
//
//        PotentialStudentEform student = psEformService.update(pse, id)
//                .orElseThrow(NotFoundException::new);
//
//        return Response
//                .ok(addHypermediaLinks(student))
//                .build();
//    }
//
//    @DELETE
//    @Path("{id: \\d+}")
//    @RolesAllowed("admin")
//    public Response delete(@PathParam("id") Long id) {
//        psEformService.delete(id);
//
//        return Response
//                .ok()
//                .build();
//    }
//
//    private JsonObject addHypermediaLinks(PotentialStudentEform psEform) {
//        URI self = resourceUriBuilder
//                .createResourceUri(PotentialStudentEformResource.class, "getById",
//                        psEform.getId(), uriInfo);
//
//        URI others = resourceUriBuilder
//                .createResourceUri(PotentialStudentEformResource.class, "getAll",
//                        uriInfo);
//
//        return Json.createObjectBuilder()
//                .add("email", psEform.getPotentialStudent().getEmail())
//                .add("fullName", psEform.getPotentialStudent().getFullName())
//                .add("phone", psEform.getPotentialStudent().getPhone())
//                .add("dateContact", psEform.getDateContact() != null ? psEform.getDateContact().toString() : "")
//                .add("dateSignUp", psEform.getDateSignUp() != null ? psEform.getDateSignUp().toString() : "")
//                .add("_links", Json.createArrayBuilder()
//                        .add(Json.createObjectBuilder()
//                                .add("rel", "self")
//                                .add("href", self.toString()))
//                        .add(Json.createObjectBuilder()
//                                .add("rel", "all")
//                                .add("href", others.toString())
//                        )
//                ).build();
//    }
//
//}
