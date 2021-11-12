package at.srfg.robxtask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.io.Serial;

@Configuration
@EnableCircuitBreaker
@EnableAutoConfiguration
@EnableFeignClients
@RestController
@SpringBootApplication
@EnableOpenApi
@ComponentScan(
        basePackages = {"at.srfg.robxtask", "at.srfg.robxtask.swagger", "at.srfg.robxtask.swagger.api", "at.srfg.robxtask.swagger.model", "io.swagger.configuration"}
)
public class RegistrationServiceApp implements CommandLineRunner {
    public RegistrationServiceApp() {
    }

    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        (new SpringApplication(RegistrationServiceApp.class)).run(args);
    }

    static class ExitException extends RuntimeException implements ExitCodeGenerator {
        @Serial
        private static final long serialVersionUID = 1L;

        ExitException() {
        }

        public int getExitCode() {
            return 10;
        }
    }

}
