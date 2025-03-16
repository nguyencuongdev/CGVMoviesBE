package cgv_cinemas_ticket.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    private final String[] publicEndpoints = new String[]{
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/auth/facebook/login",
            "/api/v1/auth/facebook/login/callback",
            "/api/v1/auth/google/login",
            "/api/v1/auth/google/login/callback",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //config spring security filter chain: csrf, authentication, authorization.
        // any request that match the public endpoints will unauthenticated.
        // any request that does not match the public endpoints will be authenticated.
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(
                request -> request.requestMatchers(publicEndpoints).permitAll()
                        .anyRequest().authenticated()
        );
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        // configure CORS filter.
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
