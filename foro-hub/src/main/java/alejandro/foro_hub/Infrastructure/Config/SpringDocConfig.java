package alejandro.foro_hub.Infrastructure.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("Foro-hub project OpenAPI Docs")
                        .version("1.0.0")
                        .description("""
                                Foro hub es un proyecto de código abierto realizado en el curso de alura oracle.
                                La información presente contiene información sobre cómo consumir los endpoints de la api
                                siguiendo un esquema de autenticación con jwt.
                                """)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .termsOfService("""
                                    Foro hub es un proyecto bajo la licencia Apache 2.0 y realizado en el curso de Alura Oracle.
                                    La información presente contiene detalles sobre cómo consumir los endpoints de la API
                                    siguiendo un esquema básico de autenticación con JWT.
                                
                                    Al ser de dominio público, esta API se ofrece como proyecto académico, por lo cual el autor
                                    no se hace responsable del uso indebido del código. Es deber de quien lo usa darle el trato adecuado.
                                    Téngase en cuenta que los usuarios y demás información son ficticios, creados como apoyo para
                                    la documentación oficial y el correcto entendimiento del usuario final.
                                """)
                );
    }
}
