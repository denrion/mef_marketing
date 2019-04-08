package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.config.DuplicateEmailException;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.entity.PotentialStudentMail;
import com.github.denrion.mef_marketing.service.PotentialStudentMailService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

import javax.inject.Inject;
import javax.validation.Valid;
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
    @Path("{id: \\d+}")
    public Response getPSById(@PathParam("id") Long id) {
        PotentialStudentMail student = psMailService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addPSMail(@BeanParam @Valid PotentialStudent ps,
                              @BeanParam @Valid PotentialStudentMail psm) {

        // TODO -> FIND A MORE EFFICIENT WAY TO DO THIS
        if (studentService.isEmailAlreadyInUse(ps.getEmail())) {
            throw new DuplicateEmailException("This email already exists");
        }

        // ***************************************** TEST DATA ******************************************** //
        PotentialStudentMail psMail1 = psMailService.createPSMail("email1@gmail.com", "phone1", "Student1",
                "2019-04-07", "2019-04-07",
                "mef@gmail.com", "2019-04-07", BigDecimal.valueOf(1200.00));
        psMailService.save(psMail1);

        PotentialStudentMail psMai2 = psMailService.createPSMail("email2@gmail.com", "phone2", "Student2",
                "2019-04-08", "2019-04-08",
                "mef@gmail.com", "2019-04-08", BigDecimal.valueOf(1200.00));
        psMailService.save(psMai2);
        // ****************************************** DELETE LATER **************************************** //

        psm.setPotentialStudent(ps);

        return Response
                .ok(psMailService.save(psm))
                .build();


    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePSMail(@PathParam("id") Long id,
                                 @BeanParam @Valid PotentialStudent ps,
                                 @BeanParam @Valid PotentialStudentMail psm) {

        if (studentService.isEmailAlreadyInUse(ps.getEmail())) {
            throw new DuplicateEmailException("This email already exists");
        }

        psm.setPotentialStudent(ps);

        PotentialStudentMail studentByMail = psMailService.update(psm, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(studentByMail)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deletePSMail(@PathParam("id") Long id) {
        psMailService.delete(id);

        return Response
                .ok()
                .build();
    }
}
