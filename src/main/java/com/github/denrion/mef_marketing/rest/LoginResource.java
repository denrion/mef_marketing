package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.security.SecurityUtil;
import com.github.denrion.mef_marketing.service.AppUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

import static com.github.denrion.mef_marketing.security.SecurityUtil.BEARER;

@Path("login")
public class LoginResource {

    @Inject
    AppUserService userService;

    @Inject
    SecurityUtil securityUtil;

    @Context
    private UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@NotBlank @FormParam("username") String username,
                          @NotBlank @FormParam("password") String password) {

        AppUser authUser = userService.authenticateUser(username, password);

        String token = generateToken(authUser);

        return Response
                .ok()
                .header(HttpHeaders.AUTHORIZATION, BEARER + " " + token)
                .build();
    }

    private String generateToken(AppUser authUser) {
        Key key = securityUtil.getSecurityKey();

        return Jwts.builder()
                .setSubject(authUser.getUsername())
                .claim("role", authUser.getRole())
                .setIssuedAt(new Date())
                .setIssuer(uriInfo.getBaseUri().toString())
                .setAudience(uriInfo.getAbsolutePath().toString())
                .setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}
