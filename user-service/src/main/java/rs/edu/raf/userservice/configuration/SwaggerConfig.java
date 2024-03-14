package rs.edu.raf.userservice.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String APP_TITLE = "user-service";
    private final String APP_DESCRIPTION = "API za korisnicki servis";

    private final String APP_API_VERSION = "1.0";
    private final String APP_LICENSE = "Licenca";
    private final String APP_LICENSE_URL = "Licenca";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(APP_TITLE)
                        .description(APP_DESCRIPTION)
                        .version(APP_API_VERSION)
                        .license(new License().name(APP_LICENSE).url(APP_LICENSE_URL)));
    }

}