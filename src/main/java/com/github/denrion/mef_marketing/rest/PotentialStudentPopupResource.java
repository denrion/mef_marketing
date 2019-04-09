package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.entity.PotentialStudentPopup;
import com.github.denrion.mef_marketing.service.PotentialStudentPopupService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("potentialStudentsPopup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentPopupResource {

    @Inject
    PotentialStudentPopupService psPopupService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllPS() {
        return Response
                .ok(psPopupService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getPSById(@PathParam("id") Long id) {
        PotentialStudentPopup student = psPopupService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createPS(@BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentPopup psPopup) {

        // ***************************************** TEST DATA ******************************************** //
        PotentialStudentPopup ps1 = psPopupService.createPSPopup("email7@gmail.com", "phone7", "Student7",
                "2019-04-07", "2019-04-07");
        psPopupService.save(ps1);

        PotentialStudentPopup ps2 = psPopupService.createPSPopup("email8@gmail.com", "phone8", "Student8",
                "2019-04-08", "2019-04-08");
        psPopupService.save(ps2);
        // ****************************************** DELETE LATER **************************************** //

        psPopup.setPotentialStudent(ps);
        psPopupService.save(psPopup);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(psPopup.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(psPopup)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePS(@PathParam("id") Long id,
                             @BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentPopup psPopup) {

        psPopup.setPotentialStudent(ps);

        PotentialStudentPopup student = psPopupService.update(psPopup, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deletePS(@PathParam("id") Long id) {
        psPopupService.delete(id);

        return Response
                .ok()
                .build();
    }
}
