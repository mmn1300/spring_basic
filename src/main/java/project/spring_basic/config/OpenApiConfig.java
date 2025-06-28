package project.spring_basic.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("spring_basic 게시판 API 목록")
                    .description(null)
                    .version("v1.0.0")
                )
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8080")
                        .description("개발용 서버")
                ));
    }
    
}
