package cz.cvut.fit.tjv.social_network.server;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${application.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        Server prodServer = new Server()
                .url(prodUrl)
                .description("Server URL in Production environment");

        Contact contact = new Contact()
                .email("majewraf@fit.cvut.cz")
                .name("Rafal Majewski");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("API for TJV course REST API")
                .version("1.0")
                .description("This API exposes endpoints to manage tutorials.")
                .contact(contact)
                .license(mitLicense);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Use the Bearer token for authentication.");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .openapi("3.0.0") // Specify the OpenAPI version
                .info(info)
                .servers(List.of(prodServer))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Bearer Authentication", securityScheme);
    }
}