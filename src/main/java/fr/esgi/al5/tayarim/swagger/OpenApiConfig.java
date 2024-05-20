package fr.esgi.al5.tayarim.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe permettant la configuration de swagger-ui.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Fonction permettant de modifier certains paramètres de swagger-ui.
   *
   * @return L'objet qui représente le swagger-ui
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                    .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
        .info(new Info().title("API Documentation").version("1.0"));
  }
}