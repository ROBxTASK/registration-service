package at.srfg.robxtask.registration.openapi;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


/**
 * Home redirection to OpenAPI api documentation
 */
@RestController
@Api(tags = {"home"})
public class HomeController {

    @Autowired
    private final NativeWebRequest request;

    @Autowired
    public HomeController(NativeWebRequest request) {
        this.request = request;
    }

    @RequestMapping("/")
    public RedirectView index() {
        StringBuffer url = ((HttpServletRequest) request.getNativeRequest()).getRequestURL();
        url.append("swagger-ui/index.html");
        return new RedirectView(String.valueOf(url));
    }


}
