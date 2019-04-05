package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.service.AdminUserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminUserREST {

    @Inject
    AdminUserService adminUserService;

    @GET
    public Response getAllUsers() {
//        AdminUser user = new AdminUser();
//        user.setFullName("Administrator");
//        user.setUsername("admin");
//        user.setPassword("admin");


        return Response
                .ok(adminUserService.getAll())
                .status(Response.Status.OK)
                .build();
    }
}
