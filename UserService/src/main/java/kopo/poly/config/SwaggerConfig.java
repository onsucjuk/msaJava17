package kopo.poly.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    private Info apiInfo() {
        return new Info()
                .title("UserService")
                .description("User Service Dexcription!!") // 문서 설명
                .contact(new Contact().name("Prof. Jeonguk Choi") // 명세서 작성자
                        .email("onsucjuk@naver.com")
                        .url("https://github.com/onsucjuk/msaJava17")
                )
                .license(new License()
                        .name("MSA 프로젝트"))
                .version("1.0.0");
    }

    @Bean
    public OpenAPI openAPI() { return new OpenAPI().components(new Components()).info(apiInfo());}
}
