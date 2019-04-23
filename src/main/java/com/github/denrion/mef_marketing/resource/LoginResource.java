package com.github.denrion.mef_marketing.resource;

import com.github.denrion.mef_marketing.config.security.SecurityUtil;
import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.service.AppUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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

import static com.github.denrion.mef_marketing.config.security.SecurityUtil.BEARER;

@Tag(name = "Login Resource",
        description = "All users are required to login through this resource before using the application")
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

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "The request has succeeded",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "204",
                            description = "There is no content to send for this request",
                            content = @Content(example = "No content")

                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )

            }
    )
    @Operation(summary = "Login",
            description = "Logs in a user if the credentials are correct. " +
                    "Returns a JWT token that lasts for 15 minutes, which must be used to access rest of the application")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @RequestBody(content = @Content(
                    mediaType = MediaType.APPLICATION_FORM_URLENCODED,
                    schema = @Schema(
                            name = "login",
                            example = "{\"password\": \"admin1\", \"username\": \"admin1\"}")))
            @NotBlank @FormParam("username") String username,
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
