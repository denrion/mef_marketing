//package com.github.denrion.mef_marketing.resource;
//
//import com.github.denrion.mef_marketing.entity.PotentialStudentMail;
//import com.github.denrion.mef_marketing.config.security.Auth;
//import com.github.denrion.mef_marketing.service.PotentialStudentMailService;
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
//@Tag(name = "PotentialStudentMailResource", description = "CRUD for potential students who contacted by email")
//@Path("mail")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
//@Auth
//public class PotentialStudentMailResource {
//
//    @Inject
//    PotentialStudentMailService psMailService;
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
//        final List<PotentialStudentMail> students = psMailService.getAll();
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
//        PotentialStudentMail student = psMailService.getById(id)
//                .orElseThrow(NotFoundException::new);
//
//        return Response
//                .ok(addHypermediaLinks(student))
//                .build();
//    }
//
//    @POST
//    @RolesAllowed({"user", "admin"})
//    public Response create(@Valid PotentialStudentMail psm) {
//
//        final PotentialStudentMail student = psMailService.save(psm);
//
//        return Response
//                .ok()
//                .status(Response.Status.CREATED)
//                .entity(addHypermediaLinks(student))
//                .build();
//    }
//
//    @PUT
//    @Path("{id: \\d+}")
//    @RolesAllowed({"user", "admin"})
//    public Response update(@PathParam("id") Long id,
//                           @Valid PotentialStudentMail psm) {
//
//        PotentialStudentMail student = psMailService.update(psm, id)
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
//        psMailService.delete(id);
//
//        return Response
//                .ok()
//                .build();
//    }
//
//    private JsonObject addHypermediaLinks(PotentialStudentMail psMail) {
//        URI self = resourceUriBuilder
//                .createResourceUri(PotentialStudentMailResource.class, "getById",
//                        psMail.getId(), uriInfo);
//
//        URI others = resourceUriBuilder
//                .createResourceUri(PotentialStudentMailResource.class, "getAll",
//                        uriInfo);
//
//        return Json.createObjectBuilder()
//                .add("email", psMail.getPotentialStudent().getEmail())
//                .add("fullName", psMail.getPotentialStudent().getFullName())
//                .add("phone", psMail.getPotentialStudent().getPhone())
//                .add("dateMailReceived", psMail.getDateMailReceived() != null ? psMail.getDateMailReceived().toString() : "")
//                .add("dateMailReceivedOnUpis", psMail.getDateMailReceivedOnUpis() != null ? psMail.getDateMailReceivedOnUpis().toString() : "")
//                .add("dateReply", psMail.getDateReply() != null ? psMail.getDateReply().toString() : "")
//                .add("emailWhichReceived", psMail.getEmailWhichReceived())
//                .add("price", psMail.getPrice())
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
//}
