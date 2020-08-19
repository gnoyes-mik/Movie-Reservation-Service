package kr.co.leadsoft.mrmserver;

import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableFeignClients
@SpringBootApplication
@EnableSwagger2
public class MrmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrmServerApplication.class, args);
    }

    /**
     * Swagger2 API 문서에서 필요없는 API 제거
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("(/actuator.*|/error|/)")))
                .build();
    }


    @Controller
    class SwaggerWelcome {
        @GetMapping("/")
        public String redirect() {
            return "redirect:/swagger-ui.html";
        }
    }

}
