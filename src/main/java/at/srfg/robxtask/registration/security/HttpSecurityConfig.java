package at.srfg.robxtask.registration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity
public class HttpSecurityConfig {

    @Order(1)
    @Configuration
    public static class ApiKeyBasedSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            final DeviceConfig config = getDeviceConfig();
            http
                    .regexMatcher("/device(s)?.*").csrf().disable()
                    .addFilter(new ApiKeyFilter(config.getApiKeyName(), config.getApiKeyValue()));
        }

        @Bean
        @ConfigurationProperties(prefix = "robxtask.registration-service.device")
        public DeviceConfig getDeviceConfig() {
            return new DeviceConfig();
        }

    }

    @Order(2)
    @Configuration
    public static class OpenIdConnectBasedSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.regexMatcher("/task(s)?.*")
                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                    .csrf().disable()
                    .authorizeRequests().anyRequest().authenticated();
        }
    }

    @Order(3)
    @Configuration
    public static class PublicAccessSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/",
                    "/public",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v2/api-docs",
                    "/v3/api-docs",
                    "/version").permitAll();
        }
    }

}