package loolu.loolu_backend.security.sec_config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import loolu.loolu_backend.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cart").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/cart/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/upload/photo").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/upload/photo/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/profile", "/api/auth/refresh", "/api/auth/logout").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://clownfish-app-sfegf.ondigitalocean.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Loolu demo app")
                        .description("Loolu application for JSON web tokens")
                        .version("1.0.0").contact(new Contact().name("Sandor Ivanyo")
                                .email("iskander.sancosjak@gmail.com").url("https://github.com/SancosJak"))
                        .license(new License().name("@SashaI")
                                .url("https://github.com/SancosJak")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
