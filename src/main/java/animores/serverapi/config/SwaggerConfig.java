package animores.serverapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // HTTPS
        Server httpsServer = new Server();

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(httpsServer));
    }

    private Info apiInfo() {
        return new Info()
                .title("Fitingle API")
                .description("Fitingle API")
                .version("1.0.0");
    }
}