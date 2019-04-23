package com.github.denrion.mef_marketing.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "MEF Marketing Information System",
        version = "1.0.0",
        contact = @Contact(
                name = "Nemanja Kuzmanovic",
                email = "kuzmaneca@gmail.com")
),
        servers = {
                @Server(url = "/mef_marketing/api/v1", description = "localhost")
        }
)
@ApplicationPath("api/v1")
public class JAXRSConfiguration extends Application {

}
