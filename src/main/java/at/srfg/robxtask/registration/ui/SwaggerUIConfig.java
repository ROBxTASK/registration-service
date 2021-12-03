package at.srfg.robxtask.registration.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.List;

@Configuration
public class SwaggerUIConfig {

    @Bean
    public Docket openApiDeviceRegistrationService() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("device-group")
                .select()
                .apis(RequestHandlerSelectors.basePackage("at.srfg.robxtask.registration.openapi.device"))
                .paths(PathSelectors.regex(".*/device(s)?.*"))
                .build()
                .apiInfo(apiInfo()).securitySchemes(securitySchemes());
    }

    @Bean
    public Docket openApiTaskRegistrationService() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("task-group")
                .select()
                .apis(RequestHandlerSelectors.basePackage("at.srfg.robxtask.registration.openapi.task"))
                .paths(PathSelectors.regex(".*/task(s)?.*")
                        .or(PathSelectors.regex(".*/process(es)?.*")))
                .build()
                .apiInfo(apiInfo()).securitySchemes(securitySchemes());
    }

    @Bean
    public Docket openApiRegistrationService() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("all")
                .select()
                .apis(RequestHandlerSelectors.basePackage("at.srfg.robxtask.registration.openapi"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).securitySchemes(securitySchemes());
    }

    private List<SecurityScheme> securitySchemes() {
        return List.of(
                HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("register_task_auth").build(),
                new ApiKey("register_device_key", "register_device_key", ApiKeyVehicle.QUERY_PARAM.getValue())
        );
    }

    ApiInfo apiInfo() {
        // NB: needs to be manually synced with the input file openapi3.yml
        return new ApiInfoBuilder()
                .title("ROBxTASK Registration Service")
                .description("This is the interface definition of the registration service for ROBxTASK (registration-service).")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version("1.1.0")
                .contact(new Contact("", "", "fstroh+robxtask@gmail.com"))
                .build();
    }
}
