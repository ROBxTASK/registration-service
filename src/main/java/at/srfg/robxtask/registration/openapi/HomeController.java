package at.srfg.robxtask.registration.openapi;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Home redirection to OpenAPI api documentation
 */
@RestController
@Api(tags = {"home"})
public class HomeController {

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/swagger-ui/index.html", true);
    }

}
