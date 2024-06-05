package loolu.loolu_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Loolu Service Backend",
                description = "Loolu Backend API application for JSON web tokens",
                version = "1.0.0",
                contact = @Contact(
                        name = "Sandor Ivanyo",
                        email = "iskander.sancosjak@gmail.com",
                        url = "https://github.com/SancosJak"
                )
        )
)
public class SwaggerConfig {
}