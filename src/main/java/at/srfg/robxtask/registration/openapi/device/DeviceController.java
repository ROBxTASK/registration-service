package at.srfg.robxtask.registration.openapi.device;

import at.srfg.robxtask.registration.exception.DeviceAlreadyExistsException;
import at.srfg.robxtask.registration.exception.DeviceNotAcceptableException;
import at.srfg.robxtask.registration.exception.DeviceNotFoundException;
import at.srfg.robxtask.registration.openapi.ApiResponseUtil;
import at.srfg.robxtask.registration.openapi.api.DeviceApi;
import at.srfg.robxtask.registration.openapi.model.Device;
import at.srfg.robxtask.registration.persistence.MongoConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
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

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@RestController
@Api(tags = {"device"})
public class DeviceController implements DeviceApi {

    private final NativeWebRequest request;

    @Autowired
    private MongoConnector mongo;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Autowired
    public DeviceController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    @Override
    public ResponseEntity<Void> addDevice(Device body) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    MongoCollection<Document> devices = getDeviceCollection();
                    final Document searchDoc = new Document("DeviceID", body.getDeviceID());
                    // DeviceOwner + DeviceID build the unique key
                    if (body.getDeviceOwner() != null) {
                        searchDoc.put("DeviceOwner", body.getDeviceOwner());
                    }
                    Document device = devices.find(searchDoc).first();
                    if (device != null) {
                        throw new DeviceAlreadyExistsException(body.getDeviceID());
                    }
                    try {
                        devices.insertOne(Document.parse(mapperBuilder.build().writeValueAsString(body)));
                        break; // insert only once, even if more content-types are in request
                    } catch (JsonProcessingException e) {
                        throw new DeviceNotAcceptableException(e.getMessage());
                    }
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Device> getDeviceById(String id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    try {
                        MongoCollection<Document> devicesCol = getDeviceCollection();
                        final Document device = devicesCol.find(eq("DeviceID", id))
                                .projection(Projections.fields(Projections.excludeId()))
                                .first();
                        if (device == null) {
                            throw new DeviceNotFoundException(id);
                        }
                        ApiResponseUtil.setContentResponse(
                                request,
                                "application/json",
                                mapperBuilder.build().writeValueAsString(device));
                        break;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateDevice(Device body) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    try {
                        MongoCollection<Document> devices = getDeviceCollection();
                        final FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().upsert(true);
                        final Document searchDoc = new Document("DeviceID", body.getDeviceID());
                        final Document replacement = Document.parse(mapperBuilder.build().writeValueAsString(body));
                        devices.findOneAndReplace(
                                searchDoc,
                                replacement,
                                options);
                        break;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delDeviceById(String id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    MongoCollection<Document> devicesCol = getDeviceCollection();
                    final Document device = devicesCol.findOneAndDelete(eq("DeviceID", id));
                    if (device == null) {
                        throw new DeviceNotFoundException(id);
                    }
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private MongoCollection<Document> getDeviceCollection() {
        return mongo.getCollection("devices");
    }

}