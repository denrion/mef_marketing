package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.entity.PotentialStudentEform;
import com.github.denrion.mef_marketing.service.PotentialStudentEformService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("potentialStudentsEform")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentEformResource {

    @Inject
    PotentialStudentEformService psEformService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllPS() {
        return Response
                .ok(psEformService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getPSById(@PathParam("id") Long id) {
        PotentialStudentEform student = psEformService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createPS(@BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentEform pse) {

        // ***************************************** TEST DATA ******************************************** //
        PotentialStudentEform ps1 = psEformService.createPSEform("email5@gmail.com", "phone5", "Student5",
                "2019-04-07", "2019-04-07");
        psEformService.save(ps1);

        PotentialStudentEform ps2 = psEformService.createPSEform("email6@gmail.com", "phone6", "Student6",
                "2019-04-08", "2019-04-08");
        psEformService.save(ps2);
        // ****************************************** DELETE LATER **************************************** //

        pse.setPotentialStudent(ps);
        psEformService.save(pse);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(pse.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(pse)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePS(@PathParam("id") Long id,
                             @BeanParam @Valid PotentialStudent ps,
                             @BeanParam @Valid PotentialStudentEform pse) {

        pse.setPotentialStudent(ps);

        PotentialStudentEform student = psEformService.update(pse, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deletePS(@PathParam("id") Long id) {
        psEformService.delete(id);

        return Response
                .ok()
                .build();
    }
}
