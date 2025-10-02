package nl.laura.boekenapi.config;

import nl.laura.boekenapi.filter.JwtRequestFilter;
import nl.laura.boekenapi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfig {

    // Password hashing (blijft zoals je had)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager met jouw UserDetailsService (blijft zoals je had)
    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder encoder
    ) {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    // DEV-profiel: alles open + H2-console
    @Bean
    @Profile("dev")
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .build();
    }

    // PROD-profiel: JWT, stateless, endpoint-regels + admin-deur
    @Bean
    @Profile("!dev")
    public SecurityFilterChain prodFilterChain(HttpSecurity http, JwtRequestFilter jwt) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(b -> b.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                        .requestMatchers("/authenticated").authenticated()

                        // Admin routes
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Books
                        .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**").authenticated()
                        .requestMatchers(HttpMethod.POST,   "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        // Files
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("ADMIN")
                        .requestMatchers("/api/files/**").hasAnyRole("ADMIN", "USER")

                        // Library
                        .requestMatchers("/api/me/library/**").hasRole("USER")

                        // Default
                        .anyRequest().authenticated()
                );

        // JWT-filter vóór de standaard Username/Password filter
        http.addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

