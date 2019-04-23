package com.github.denrion.mef_marketing.resource;

import com.github.denrion.mef_marketing.entity.AppUser;
import com.github.denrion.mef_marketing.service.AppUserService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterStyle;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.links.Link;
import org.eclipse.microprofile.openapi.annotations.links.LinkParameter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Tag(name = "User resource", description = "Resource for creating, updating, deleting and getting users")
//@Auth
//@RolesAllowed("admin")
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppUserResource {

    @Inject
    AppUserService userService;

    @Context
    private UriInfo uriInfo;

    @Inject
    private ResourceUriBuilder resourceUriBuilder;

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "The request has succeeded",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = AppUser.class)),
                            links = {
                                    @Link(
                                            name = "self",
                                            operationRef = "#/api/v1/users/{id}",
                                            description = "Get user by id",
                                            parameters = @LinkParameter(name = "id", expression = "{id}")
                                    ),
                                    @Link(
                                            name = "collection",
                                            operationRef = "#/api/v1/users",
                                            description = "Get all users"
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "204",
                            description = "There is no content to send for this request",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response. Login required.",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Server can not find the requested resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )

            }
    )
    @Operation(summary = "Get all users",
            description = "This method returns a list of all users")
    @GET
    @Path("")
    public Response getAll(
            @Parameter(
                    name = "from", in = ParameterIn.QUERY,
                    description = "First Result. Default value 1",
                    example = "0",
                    style = ParameterStyle.SIMPLE,
                    schema = @Schema(type = SchemaType.INTEGER))
            @QueryParam("from") @DefaultValue("1") @Min(value = 1) int from,
            @Parameter(
                    name = "to", in = ParameterIn.QUERY,
                    description = "Max Result. Default value 10",
                    example = "10",
                    style = ParameterStyle.SIMPLE,
                    schema = @Schema(type = SchemaType.INTEGER))
            @QueryParam("to") @DefaultValue("10") @Min(value = 1) int to) {

        List<AppUser> users = userService.getAll(from, to);

        if (users == null || users.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        JsonArray data = users.stream()
                .map(this::toJson)
                .collect(JsonCollectors.toJsonArray());


        return Response
                .ok()
                .entity(data)
                .build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "The request has succeeded",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON),
                            links = {
                                    @Link(
                                            name = "self",
                                            operationRef = "#/api/v1/users/{id}",
                                            description = "Get user by id",
                                            parameters = @LinkParameter(name = "id", expression = "{id}")
                                    ),
                                    @Link(
                                            name = "collection",
                                            operationRef = "#/api/v1/users",
                                            description = "Get all users"
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response. Login required.",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Server can not find requested resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )
            }
    )
    @Operation(summary = "Get user by provided id",
            description = "This method returns a single user")
    @GET
    @Path("{id}")
    public Response getById(
            @Parameter(
                    name = "id", in = ParameterIn.PATH,
                    description = "Id of the user to be retrieved",
                    example = "1",
                    style = ParameterStyle.SIMPLE,
                    schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id") @Min(value = 1) Long id) {

        AppUser user = userService.getById(id)
                .orElseThrow(() -> new NotFoundException("The user with id " + id + " does not exist"));

        return Response
                .ok(toJson(user))
                .build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "The request has succeeded and a new resource has been created as a result of it",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = AppUser.class)),
                            links = {
                                    @Link(
                                            name = "self",
                                            operationRef = "#/api/v1/users/{id}",
                                            description = "Get user by id",
                                            parameters = @LinkParameter(name = "id", expression = "{id}")
                                    ),
                                    @Link(
                                            name = "collection",
                                            operationRef = "#/api/v1/users",
                                            description = "Get all users"
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response. Login required.",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Server can not find requested resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "405",
                            description = "The request method is known by the server but has been disabled and cannot be used",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "412",
                            description = "The client has indicated preconditions in its headers which the server does not meet",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "415",
                            description = "The media format of the requested data is not supported by the server",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )
            }
    )
    @Operation(summary = "Create a new user",
            description = "This method creates a new user")
    @POST
    public Response create(
            @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUser.class)))
            @Valid AppUser user) {

        AppUser appUser = userService.save(user);

        return Response
                .ok()
                .status(Response.Status.CREATED)
                .entity(toJson(appUser))
                .build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "The request has succeeded",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = AppUser.class)),
                            links = {
                                    @Link(
                                            name = "self",
                                            operationRef = "#/api/v1/users/{id}",
                                            description = "Get user by id",
                                            parameters = @LinkParameter(name = "id", expression = "{id}")
                                    ),
                                    @Link(
                                            name = "collection",
                                            operationRef = "#/api/v1/users",
                                            description = "Get all users"
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response. Login required.",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Server can not find requested resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "405",
                            description = "The request method is known by the server but has been disabled and cannot be used",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "412",
                            description = "The client has indicated preconditions in its headers which the server does not meet",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "415",
                            description = "The media format of the requested data is not supported by the server",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )
            }
    )
    @Operation(summary = "Update an existing user",
            description = "This method updates an existing user which id was specified as a parameter")
    @PUT
    @Path("{id}")
    public Response update(
            @Parameter(name = "id", in = ParameterIn.PATH,
                    description = "Id of the user to be updated.",
                    example = "1",
                    style = ParameterStyle.SIMPLE,
                    schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id") @Min(value = 1) Long id,
            @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AppUser.class)))
            @Valid AppUser user) {

        AppUser appUser = userService.update(user, id)
                .orElseThrow(IllegalStateException::new);

        return Response
                .ok()
                .entity(toJson(appUser))
                .build();
    }

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "The request has succeeded",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "401",
                            description = "Authentication is needed to get requested response. Login required.",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "403",
                            description = "Client does not have access rights to the content so server is rejecting to give proper response",
                            content = @Content(example = "No content")
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Server can not find requested resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "405",
                            description = "The request method is known by the server but has been disabled and cannot be used",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "415",
                            description = "The media format of the requested data is not supported by the server",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON)
                    ),
                    @APIResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(example = "No content")
                    )

            }
    )
    @Operation(summary = "Delete an existing user",
            description = "This method updates an existing user which id was specified as a parameter")
    @DELETE
    @Path("{id}")
    public Response delete(
            @Parameter(
                    name = "id", in = ParameterIn.PATH,
                    description = "Id of the user to be deleted.",
                    example = "1",
                    style = ParameterStyle.SIMPLE,
                    schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id") @Min(value = 1) Long id) {

        userService.delete(id);

        return Response
                .ok()
                .build();
    }

    private JsonObject toJson(AppUser user) {
        URI self = resourceUriBuilder
                .createResourceUri(AppUserResource.class, "getById",
                        user.getId(), uriInfo);

        URI collection = resourceUriBuilder
                .createResourceUri(AppUserResource.class, "getAll",
                        uriInfo);

        return user.toJson()
                .add("_links", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("rel", "self")
                                .add("href", self.toString()))
                        .add(Json.createObjectBuilder()
                                .add("rel", "collection")
                                .add("href", collection.toString())
                        )
                ).build();
    }
}
