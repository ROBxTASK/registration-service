package at.srfg.robxtask.registration.openapi;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class OpenAPIParserTest {

    @Before
    public void setup() {
    }

    @Test
    public void testOpenApiSpec() throws Exception {
        SwaggerParseResult result = new OpenAPIV3Parser()
                .readLocation("openapi3.yaml", null, new ParseOptions());

        assertNotNull(result);
        assertNotNull(result.getOpenAPI());
        OpenAPI openAPI = result.getOpenAPI();
        assertEquals("1.1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getServers());
        assertFalse(openAPI.getServers().isEmpty());
        assertNotNull(openAPI.getInfo().getTitle());
        assertNotNull(openAPI.getInfo().getLicense().getName());
        assertNotNull(openAPI.getPaths().get("/devices").getGet().getResponses().get("200").getDescription());
        assertNotNull(openAPI.getPaths().get("/tasks").getGet().getResponses().get("200").getDescription());
    }
}
