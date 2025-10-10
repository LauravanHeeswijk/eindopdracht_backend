package nl.laura.boekenapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.laura.boekenapi.service.CustomUserDetailsService;
import nl.laura.boekenapi.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (uri.startsWith("/api/health")
                || uri.startsWith("/h2-console")
                || uri.startsWith("/api/auth/authenticate")) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("GET".equalsIgnoreCase(method)) {
            if (uri.startsWith("/api/books")
                    || uri.startsWith("/api/authors")
                    || uri.startsWith("/api/categories")
                    || uri.startsWith("/api/files/")) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                email = jwtUtil.extractUsername(token);
            } catch (Exception ignored) {
                email = null; // kapotte/ongeldige token -> ga anoniem verder, NIET zelf 401/403 geven
            }
        }

        // 3) Als we een geldige user hebben, zet authenticatie in de context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            String ok = jwtUtil.usernameIfValid(token, userDetails);
            if (ok != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 4) Altijd doorzetten
        filterChain.doFilter(request, response);
    }
}



//package nl.laura.boekenapi.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import nl.laura.boekenapi.service.CustomUserDetailsService;
//import nl.laura.boekenapi.utils.JwtUtil;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    // ⬇️ Belangrijk: publieke paden overslaan
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String uri = request.getRequestURI();
//        String method = request.getMethod();
//
//        // open endpoints
//        if (uri.startsWith("/api/health")) return true;
//        if (uri.startsWith("/h2-console")) return true;
//        if (uri.startsWith("/api/auth/authenticate")) return true;
//
//        // publieke GET-endpoints
//        if ("GET".equalsIgnoreCase(method)) {
//            if (uri.startsWith("/api/books")
//                    || uri.startsWith("/api/authors")
//                    || uri.startsWith("/api/categories")
//                    || uri.startsWith("/api/files/")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//        String email = null;
//        String token = null;
//
//        if (header != null && header.startsWith("Bearer ")) {
//            token = header.substring(7);
//            try {
//                email = jwtUtil.extractUsername(token);
//            } catch (Exception ignored) {
//                email = null; // ongeldig token -> ga door als anoniem (niet meteen 403 geven)
//            }
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//
//            String ok = jwtUtil.usernameIfValid(token, userDetails);
//            if (ok != null) {
//                UsernamePasswordAuthenticationToken auth =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
