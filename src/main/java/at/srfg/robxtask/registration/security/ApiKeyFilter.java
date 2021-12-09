package at.srfg.robxtask.registration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ApiKeyFilter extends AnonymousAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    private final String keyName;
    private final String expectedKeyValue;

    public ApiKeyFilter(String apiKeyName, String apiKeyValue) {
        super("anonymous-with-api-key");
        this.keyName = apiKeyName;
        this.expectedKeyValue = apiKeyValue;
        if (expectedKeyValue == null || expectedKeyValue.isEmpty() || "none".equals(expectedKeyValue)) {
            final String msg = keyName + " not configured!";
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) res;
        final Map<String, String[]> params = req.getParameterMap();
        if (params == null || !params.containsKey(keyName)) {
            final String msg = "required key not in request";
            log.debug(msg);
            httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
        } else if (!expectedKeyValue.equals(params.get(keyName)[0])) {
            final String msg = "invalid key in request";
            log.debug(msg);
            httpRes.sendError(HttpServletResponse.SC_FORBIDDEN, msg);
        } else {
            log.debug("valid key named '" + keyName + "' found, access granted.");
            chain.doFilter(req, res);
        }
    }

}
