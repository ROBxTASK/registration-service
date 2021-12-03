package at.srfg.robxtask.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.io.Serial;

@Configuration
@EnableAutoConfiguration
@EnableFeignClients
@RestController
@EnableWebMvc
@EnableOpenApi
@SpringBootApplication
@ComponentScan(basePackages = {"at.srfg.robxtask.registration"})
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

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }


}
