package at.srfg.robxtask.registration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@EnableWebSecurity
public class HttpSecurityConfig {

    @Order(1)
    @Configuration
    public static class ApiKeyBasedSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            final DeviceConfig config = getDeviceConfig();
            http
                    .regexMatcher("/device(s)?.*")
                    .addFilterBefore(new ApiKeyFilter(config.getApiKeyName(), config.getApiKeyValue()), AbstractPreAuthenticatedProcessingFilter.class).csrf().disable();
        }

        @Bean
        @ConfigurationProperties(prefix = "robxtask.registration-service.device")
        public DeviceConfig getDeviceConfig() {
            return new DeviceConfig();
        }

    }

    @Order(2)
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

//    @Order(3) // should be default, if the above do not match
//    @Configuration
//    public static class OpenIdConnectBasedSecurityConfigurer extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//        }
//    }


}