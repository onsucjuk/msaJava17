package kopo.poly.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private Info apiInfo() {
        return new Info()
                .title("NoticeService")
                .description("Notice Service Description!!")
                .contact(new Contact().name("BackEnd Developer. Jeonguk Choi")
                        .email("onsucjuk@naver.com")
                        .url("https://spurious-cardamom-b9d.notion.site/BackEnd-84f8ce9018064869846d9122188cb0a2"))
                .license(new License()
                        .name("포트폴리오"))
                .version("1.0.0");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()).info(apiInfo());
    }

}
