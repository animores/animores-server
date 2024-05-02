package animores.serverapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Animores server");

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(server))
                .components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    private Info apiInfo() {
        return new Info()
                .title("Animores server API")
                .description("Animores server API for the Animores app.")
                .version("1.0.0");
    }
}