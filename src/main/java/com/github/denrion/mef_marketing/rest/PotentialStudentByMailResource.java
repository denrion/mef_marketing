package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.config.DuplicateEmailException;
import com.github.denrion.mef_marketing.entity.PotentialStudentMail;
import com.github.denrion.mef_marketing.service.PotentialStudentMailService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;


@Path("potentialStudentsByMail")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentByMailResource {

    @Inject
    PotentialStudentService studentService;

    @Inject
    PotentialStudentMailService psMailService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllPSMail() {
        return Response
                .ok(psMailService.getAll())
                .build();
    }

    @GET
    @Path("{id}")
    public Response getPSById(@PathParam("id") Long id) {
        PotentialStudentMail student =
                psMailService.getByIdWithPotentialStudent(id)
                        .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addPSMail(@FormParam("email") String email,
                              @FormParam("phone") String phone,
                              @FormParam("fullName") String fullName,
                              @FormParam("dateMailReceived") String dateMailReceived,
                              @FormParam("dateMailReceivedOnUpis") String dateMailReceivedOnUpis,
                              @FormParam("emailWhichReceived") String emailWhichReceived,
                              @FormParam("dateReply") String dateReply,
                              @FormParam("price") BigDecimal price) {

//        PotentialStudent potentialStudent = studentService.createPotentialStudent(email, phone, fullName);
//        PotentialStudentMail PS = psMailService.createMail(dateMailReceived, dateMailReceivedOnUpis,
//                emailWhichReceived, dateReply, price, potentialStudent);

        if (studentService.isEmailAlreadyInUse(email)) {
            throw new DuplicateEmailException("This email already exists");
        }

        // TEST DATA
        PotentialStudentMail PS = psMailService.createPSByMail("email@gmail.com", "phone1", "Student1",
                "2019-04-07", "2019-04-07",
                "mef1@gmail.com", "2019-04-07", BigDecimal.valueOf(1200.00));

        PotentialStudentMail student = psMailService.save(PS);

        return Response
                .ok(student)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePSMail(@PathParam("id") Long id,
                                 @FormParam("email") String email,
                                 @FormParam("phone") String phone,
                                 @FormParam("fullName") String fullName,
                                 @FormParam("dateMailReceived") String dateMailReceived,
                                 @FormParam("dateMailReceivedOnUpis") String dateMailReceivedOnUpis,
                                 @FormParam("emailWhichReceived") String emailWhichReceived,
                                 @FormParam("dateReply") String dateReply,
                                 @FormParam("price") BigDecimal price) {

        if (studentService.isEmailAlreadyInUse(email)) {
            throw new DuplicateEmailException("This email already exists");
        }

        // TEST DATA
        PotentialStudentMail potentialStudentMail =
                psMailService.createPSByMail("email1@gmail.com", "phone1", "Student1",
                        "2019-04-04", "2019-04-04",
                        "mef@gmail.com", "2019-04-04", BigDecimal.valueOf(1200.00));

        PotentialStudentMail studentByMail = psMailService.update(potentialStudentMail, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(studentByMail)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePSMail(@PathParam("id") Long id) {
        psMailService.delete(id);

        return Response
                .ok()
                .build();
    }
}
