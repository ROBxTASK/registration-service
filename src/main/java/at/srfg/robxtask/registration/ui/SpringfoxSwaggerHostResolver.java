package at.srfg.robxtask.registration.ui;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.parser.exception.ReadContentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * required due to an open springfox issue: https://github.com/springfox/springfox/issues/3483
 */
@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

    @Value("${robxtask.registration-service.swagger.external-address}")
    String hostUri;

    // TODO: provide as config value in future
    String yamlSpecFile = "openapi3.yaml";

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        final OpenAPI swagger = context.getSpecification();
        final List<Server> allServers = new ArrayList<>();
        final List<Server> inferredServers = swagger.getServers();

        if (inferredServers != null) {
            allServers.addAll(inferredServers);
        }

        allServers.add(getServerFromConfig());
        allServers.addAll(getServersFromOpenApiDef(yamlSpecFile));

        swagger.setServers(allServers);

        return swagger;
    }

    private Collection<? extends Server> getServersFromOpenApiDef(String url) throws ReadContentException {
        SwaggerParseResult result = new OpenAPIV3Parser()
                .readLocation(url, null, new ParseOptions());
        return new ArrayList<>(result.getOpenAPI().getServers());
    }

    private Server getServerFromConfig() {
        Server server = new Server();
        server.setUrl(hostUri);
        server.setDescription("configured Url");
        return server;
    }

    @Override
    public boolean supports(DocumentationType docType) {
        return docType.equals(DocumentationType.OAS_30);
    }
}