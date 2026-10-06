//////////////////////////////////////////////////////////////////
//
//  Class Name          : OpenAPIConfig
//
//  Description         : Configures OpenAPI documentation for the
//                        Digital Banking Portal REST APIs.
//
//                        It provides API information such as the
//                        application title, version, and description.
//
//                        It also configures Bearer Authentication
//                        using JWT for secured API endpoints.
//
//                        The configuration is registered as a
//                        Spring Bean for Swagger/OpenAPI support.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 23/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : OpenAPIConfig
//
//  Description         : Provides configuration for Swagger and
//                        OpenAPI documentation in the application.
//
//                        The @Configuration annotation tells Spring
//                        that this class contains configuration
//                        definitions.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 23/09/2026
//
//////////////////////////////////////////////////////////////////

@Configuration
public class OpenAPIConfig
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : customOpenAPI
    //
    //  Description         : Creates and configures the OpenAPI
    //                        documentation for the application.
    //
    //                        It defines the API title, version,
    //                        description, and JWT Bearer
    //                        authentication details.
    //
    //                        The configured OpenAPI object is
    //                        returned as a Spring Bean.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 23/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Bean
    public OpenAPI customOpenAPI()
    {
        // Create OpenAPI documentation with API information
        return new OpenAPI().info(new Info().title("Digital Banking Portal API").version("1.0")
                        .description("REST API documentation for Digital Banking Portal"))

                // Configure JWT Bearer Authentication
                .components(new Components().addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))

                // Apply Bearer Authentication to the API documentation
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))

                // Configure JWT Bearer Authentication security scheme
                .components(new Components().addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}