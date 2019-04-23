package com.github.denrion.mef_marketing.resource;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ResourceUriBuilder {

    public URI createResourceUri(Class<?> resourcesClass, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(resourcesClass)
                .build();
    }

    public URI createResourceUri(Class<?> resourcesClass, String method, UriInfo uriInfo) {
        return uriInfo.getAbsolutePathBuilder()
                .path(resourcesClass, method)
                .build();
    }

    public URI createResourceUri(Class<?> resourcesClass, String method, long id, UriInfo uriInfo) {
        return uriInfo.getAbsolutePathBuilder()
                .path(resourcesClass, method)
                .build(id);
    }

}
