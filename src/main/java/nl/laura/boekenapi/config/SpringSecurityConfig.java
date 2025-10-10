package nl.laura.boekenapi.config;

import nl.laura.boekenapi.filter.JwtRequestFilter;
import nl.laura.boekenapi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService uds,
                                                         PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http,
                                      AuthenticationProvider provider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(b -> b.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/authenticate", "/api/health", "/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/categories/**",
                                "/api/files/**"
                        ).permitAll()
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/me/library/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/books/**","/api/authors/**","/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**","/api/authors/**","/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**","/api/authors/**","/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .authenticationProvider(provider)
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        return http.build();
    }
}
