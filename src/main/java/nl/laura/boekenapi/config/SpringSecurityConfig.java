package nl.laura.boekenapi.config;

import nl.laura.boekenapi.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtRequestFilter jwt;

    public SpringSecurityConfig(JwtRequestFilter jwt) {
        this.jwt = jwt;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(b -> b.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Public
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/authenticate", "/api/health", "/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/categories/**",
                                "/api/files/**"
                        ).permitAll()

                        // Authenticated (ingelogd)
                        .requestMatchers("/api/auth/me").authenticated()

                        // === User library (TOEGEVOEGD) ===
                        .requestMatchers(HttpMethod.POST,   "/api/me/library/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/me/library/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/me/library/**").hasAnyRole("USER","ADMIN")

                        // Admin-only CRUD
                        .requestMatchers(HttpMethod.POST,   "/api/books/**","/api/authors/**","/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/books/**","/api/authors/**","/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**","/api/authors/**","/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/files/upload").hasRole("ADMIN")

                        .anyRequest().denyAll()
                );

        http.addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

