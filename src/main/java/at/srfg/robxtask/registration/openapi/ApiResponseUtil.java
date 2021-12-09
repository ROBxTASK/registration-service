package at.srfg.robxtask.registration.openapi;

import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiResponseUtil {
    public static void setContentResponse(NativeWebRequest req, String contentType, String content) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            if (res != null) {
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Type", contentType);
                res.getWriter().print(content);
            } else {
                throw new IOException("Internal Error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
