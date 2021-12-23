package at.srfg.robxtask.registration.openapi.device;

import at.srfg.robxtask.registration.openapi.ApiResponseUtil;
import at.srfg.robxtask.registration.openapi.api.DevicesApi;
import at.srfg.robxtask.registration.openapi.model.Device;
import at.srfg.robxtask.registration.persistence.MongoConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = {"devices"})
public class DevicesController implements DevicesApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DevicesController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    private static final Logger log = LoggerFactory.getLogger(DevicesController.class);

    @Autowired
    private MongoConnector mongo;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Override
    public ResponseEntity<List<Device>> getDevices() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    try {
                        ObjectMapper mapper = mapperBuilder.build();
                        MongoCollection<Document> devicesCol = getDeviceCollection();
                        final FindIterable<Document> devices = devicesCol.find().projection(Projections.fields(Projections.exclude("_id")));
                        List<Device> res = new ArrayList<>();
                        for (Document device : devices) {
                            res.add(mapper.readValue(device.toJson(), Device.class));
                        }
                        ApiResponseUtil.setContentResponse(request, "application/json", mapper.writeValueAsString(res));
                        break; // do only once, even if more content-types are in request
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return new ResponseEntity<>(HttpStatus.OK);
}

    private MongoCollection<Document> getDeviceCollection() {
        return mongo.getCollection("devices");
    }

}
