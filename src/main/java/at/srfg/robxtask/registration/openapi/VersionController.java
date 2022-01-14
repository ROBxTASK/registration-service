package at.srfg.robxtask.registration.openapi;

import at.srfg.robxtask.registration.openapi.api.VersionApi;
import at.srfg.robxtask.registration.openapi.model.Version;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@Api(tags = {"version"})
public class VersionController implements VersionApi {

    private final NativeWebRequest request;

    @Autowired
    public VersionController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    private static final Logger log = LoggerFactory.getLogger(VersionController.class);

    @Override
    public ResponseEntity<Version> getVersion() {
        final Version version = new Version()
                .serviceId("registration-service")
                .version("1.1.0");
        log.debug("version requested, returning {} {}", version.getServiceId(), version.getVersion());
        return new ResponseEntity<>(
                version,
                HttpStatus.OK);
    }
}
