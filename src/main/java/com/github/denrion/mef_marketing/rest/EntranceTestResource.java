package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AdminUser;
import com.github.denrion.mef_marketing.entity.EntranceTest;
import com.github.denrion.mef_marketing.entity.PotentialStudent;
import com.github.denrion.mef_marketing.service.AdminUserService;
import com.github.denrion.mef_marketing.service.EntranceTestService;
import com.github.denrion.mef_marketing.service.PotentialStudentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("entranceTests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EntranceTestResource {

    @Inject
    EntranceTestService testService;

    @Inject
    PotentialStudentService studentService;

    @Inject
    AdminUserService userService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAllTests() {
        return Response
                .ok(testService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getTestById(@PathParam("id") Long id) {
        EntranceTest test = testService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(test)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createTest(@BeanParam @Valid EntranceTest test,
                               @FormParam("ps_id") Long ps_id,
                               @FormParam("user_id") Long user_id) {

        AdminUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        test.setUser(user);
        test.setPotentialStudent(student);

        EntranceTest entranceTest = testService.save(test);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(entranceTest.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(entranceTest)
                .status(Response.Status.CREATED)
                .build();

    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateTest(@PathParam("id") Long id,
                               @BeanParam @Valid EntranceTest test,
                               @FormParam("ps_id") Long ps_id,
                               @FormParam("user_id") Long user_id) {

        AdminUser user = userService.getById(user_id)
                .orElseThrow(NotFoundException::new);

        PotentialStudent student = studentService.getById(ps_id)
                .orElseThrow(NotFoundException::new);

        test.setUser(user);
        test.setPotentialStudent(student);

        EntranceTest entranceTest = testService.update(test, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(entranceTest)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deleteTest(@PathParam("id") Long id) {
        testService.delete(id);

        return Response
                .ok()
                .build();
    }
}
