package at.srfg.robxtask.registration.ui;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.parser.exception.ReadContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * required due to an open springfox issue: https://github.com/springfox/springfox/issues/3483
 */
@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

    @Autowired
    private ServletContext context;

    @Value("${robxtask.registration-service.swagger.external-address}")
    private String hostUri;

    // TODO: provide as config value in future
    private String yamlSpecFile = "openapi3.yaml";

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> openApiContext) {
        final OpenAPI swagger = openApiContext.getSpecification();
        final List<Server> allServers = new ArrayList<>();

        final Collection<? extends Server> definedServers = getServersFromOpenApiDef(yamlSpecFile);
        if (definedServers.size() > 1) {
            allServers.addAll(definedServers);
        }

        final List<Server> inferredServers = swagger.getServers();
        if (inferredServers != null) {
            allServers.addAll(inferredServers);
        }

        if (hostUri != null) {
            allServers.add(getServerFromConfig());
        }

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
        server.setUrl(hostUri.replaceAll("/$", "") + context.getContextPath());
        server.setDescription("Configured Url");
        return server;
    }

    @Override
    public boolean supports(DocumentationType docType) {
        return docType.equals(DocumentationType.OAS_30);
    }
}