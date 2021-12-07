package at.srfg.robxtask.registration.ui;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

    @Value("${swagger.host}") String hostUri;

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI swagger = context.getSpecification();
        Server server = new Server();
        server.setUrl(hostUri);
        swagger.setServers(Arrays.asList(server));
        return swagger;
    }

    @Override
    public boolean supports(DocumentationType docType) {
        return docType.equals(DocumentationType.OAS_30);
    }
}