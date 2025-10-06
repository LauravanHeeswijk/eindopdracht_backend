package nl.laura.boekenapi.controller;

import jakarta.validation.Valid;
import nl.laura.boekenapi.dto.AuthenticationRequest;
import nl.laura.boekenapi.dto.AuthenticationResponse;
import nl.laura.boekenapi.service.CustomUserDetailsService;
import nl.laura.boekenapi.utils.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthenticationController(final AuthenticationManager authenticationManager,
                                    final CustomUserDetailsService userDetailsService,
                                    final JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(final Authentication authentication) {
        return ResponseEntity.ok(authentication == null ? null : authentication.getPrincipal());
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody final AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        try {
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
