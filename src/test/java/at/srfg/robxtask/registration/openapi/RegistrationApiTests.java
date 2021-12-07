package at.srfg.robxtask.registration.openapi;

import at.srfg.robxtask.registration.RegistrationServiceApp;
import com.mongodb.client.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = RegistrationServiceApp.class)
public class RegistrationApiTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ResourceLoader resourceLoader;

    private MockMvc mockMvc;

    @MockBean
    private MongoClient mongoClient;

    private static final Logger log = LoggerFactory.getLogger(RegistrationApiTests.class);

    // name and correct key need to fit with the environment configuration!
    @Value("${robxtask.registration-service.device.api-key-name}")
    private String apiKeyName;

    @Value("${robxtask.registration-service.device.api-key-value}")
    private String apiKeyValue;

    private final String exampleDeviceFilename = "example-device.json";
    private final String malformedDevice1Filename = "malformed-device-1.json";
    private final String deviceId = "2C:54:91:88:C9:E3";
    private String exampleDoc;
    private String malformedDoc1;

    @Before
    public void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity())
//                .addFilter(springSecurityFilterChain)
                .build();

        exampleDoc = readDeviceFile(exampleDeviceFilename);
        malformedDoc1 = readDeviceFile(malformedDevice1Filename);
    }

    private String readDeviceFile(String deviceFilename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + deviceFilename);
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        return new String(bdata, StandardCharsets.UTF_8);
    }

    @Test
    public void testDevicesApiAccessControl() throws Exception {
        mockMvc.perform(get("/devices")) // deny/401
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/devices?" + apiKeyName + "=wrongKey")) // deny/403
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/devices?" + apiKeyName + "=" + apiKeyValue)) // allow/200
                .andExpect(status().isOk());
    }

    @Test
    public void testDeviceApiAccessControl() throws Exception {

        mockMvc.perform(get("/device/" + deviceId + "?" + apiKeyName + "=" + apiKeyValue)) // allow/200
                .andExpect(status().isOk());

        mockMvc.perform(post("/device?" + apiKeyName + "=" + apiKeyValue)
                        .content(exampleDoc).contentType(MediaType.APPLICATION_JSON)) // allow/201
                .andExpect(status().isCreated());

        mockMvc.perform(put("/device?" + apiKeyName + "=" + apiKeyValue)
                        .content(exampleDoc).contentType(MediaType.APPLICATION_JSON)) // allow/200
                .andExpect(status().isOk());

        mockMvc.perform(get("/device/" + deviceId)) // deny/401
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/device/" + deviceId + "?" + apiKeyName + "=wrongKey")) // deny/403
                .andExpect(status().isForbidden());

    }

    @Test
    public void testVersionApiAccessControl() throws Exception {
        mockMvc.perform(get("/version")) // allow/200
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value("registration-service")
                );
    }

    @Test
    public void testDeviceApiMalformedContent() throws Exception {
        mockMvc.perform(post("/device?" + apiKeyName + "=" + apiKeyValue)
                        .content(malformedDoc1).contentType(MediaType.APPLICATION_JSON)) // deny/400
                .andExpect(status().isBadRequest());


    }

    @Test
    public void testTaskApiAccessControl() throws Exception {

        // NOT YET IMPLEMENTED: secure task-api
        log.info("task api tests not yet implemented");
        //mockMvc.perform(get("/tasks")) // deny/401
        //        .andExpect(status().isUnauthorized());

        //mockMvc.perform(get("/tasks").with(jwt())) // allow/200
        //        .andExpect(status().isOk());

    }

}