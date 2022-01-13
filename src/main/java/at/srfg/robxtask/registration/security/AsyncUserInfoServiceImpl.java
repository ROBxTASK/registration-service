package at.srfg.robxtask.registration.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AsyncUserInfoServiceImpl implements AsyncUserInfoService {


    @Value("${robxtask.registration-service.identity-service-url}")
    private String identityServiceUrl;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private static final Logger log = LoggerFactory.getLogger(AsyncUserInfoServiceImpl.class);

    @Async
    @Override
    public UserInfo resolve() throws InterruptedException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();
        String bearer = "";
        if (principal instanceof Jwt) {
            Jwt token = (Jwt) principal;
            bearer = token.getTokenValue();
        } else {
            log.debug("no valid authentication token: {}", authentication);
            throw new InvalidBearerTokenException("no valid token");
        }
        String url = identityServiceUrl.replaceAll("/$", "") + "/user-info";

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer "+ bearer);
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> result = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        try {
            if (result.getStatusCode() != HttpStatus.OK) {
                log.error("failure during user-info request from identity service");
                log.info("result: {}", result);
                throw new RuntimeException("failure during user-info request from identity service");
            }
            return mapper.readValue(result.getBody(), UserInfo.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
