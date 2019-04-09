package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.entity.PotentialStudentPhone;
import com.github.denrion.mef_marketing.service.PotentialStudentPhoneService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;

@Path("potentialStudentsPhone")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentPhoneResource {

    @Inject
    PotentialStudentPhoneService psPhoneService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllPS() {
        return Response
                .ok(psPhoneService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getByPSId(@PathParam("id") Long id) {
        PotentialStudentPhone student = psPhoneService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createPS(@BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentPhone psp) {

        // ***************************************** TEST DATA ******************************************** //
        PotentialStudentPhone ps1 = psPhoneService.createPSPhone("email3@gmail.com", "phone3", "Student3",
                "Beograd", "someone", 1
                , "2019-04-07", BigDecimal.valueOf(1200.00), "", "");
        psPhoneService.save(ps1);

        PotentialStudentPhone ps2 = psPhoneService.createPSPhone("email4@gmail.com", "phone4", "Student4",
                "Kragujevac", "someone else",
                1, "2019-04-08", BigDecimal.valueOf(1200.00), "", "");
        psPhoneService.save(ps2);
        // ****************************************** DELETE LATER **************************************** //

        psp.setPotentialStudent(ps);
        psPhoneService.save(psp);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(psp.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(psp)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePS(@PathParam("id") Long id,
                             @BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentPhone psp) {

        psp.setPotentialStudent(ps);

        PotentialStudentPhone student = psPhoneService.update(psp, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deletePS(@PathParam("id") Long id) {
        psPhoneService.delete(id);

        return Response
                .ok()
                .build();
    }
}
