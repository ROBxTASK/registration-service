package at.srfg.robxtask.registration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ApiKeyFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    private final String keyName;
    private final String expectedKeyValue;

    public ApiKeyFilter(String keyName, String expectedKeyValue) {
        this.keyName = keyName;
        this.expectedKeyValue = expectedKeyValue;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (expectedKeyValue == null || expectedKeyValue.isEmpty() || "none".equals(expectedKeyValue)) {
            final String msg = keyName + " not configured!";
            log.error(msg);
            throw new ServletException(msg);
        }
        HttpServletResponse httpRes = (HttpServletResponse) res;
        final Map<String, String[]> params = req.getParameterMap();
        if (params == null || !params.containsKey(keyName)) {
            httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (!expectedKeyValue.equals(params.get(keyName)[0])) {
            httpRes.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        chain.doFilter(req, res);
    }

}
