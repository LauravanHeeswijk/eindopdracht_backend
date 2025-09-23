// src/main/java/nl/laura/boekenapi/security/SecurityConfig.java
package nl.laura.boekenapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev") // alleen actief in het dev-profiel
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 1) H2-console toestaan
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()   // voor nu alles vrij in dev
                )
                // 2) CSRF uit (minimaal voor H2)
                //    Wil je CSRF niet overal uit? Gebruik de ignoring-variant hieronder.
                .csrf(csrf -> csrf.disable())
                // .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))

                // 3) Frames toestaan vanaf dezelfde origin (anders blijven de frames grijs)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // 4) Basic mag, maar is niet nodig als alles permitAll is
                //.httpBasic(Customizer.withDefaults())

                .build();
    }
}
