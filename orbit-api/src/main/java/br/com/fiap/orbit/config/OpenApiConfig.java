package br.com.fiap.orbit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orbitOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ORBIT API")
                        .description("Plataforma inteligente de apoio à decisão para operações críticas em ambientes extremos — FIAP Global Solution 2026/1")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Henrique Rodrigues Vespasiano")
                                .email("rm562917@fiap.com.br")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
