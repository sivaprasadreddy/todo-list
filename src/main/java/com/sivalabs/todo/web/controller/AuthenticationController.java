package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.config.ApplicationProperties;
import com.sivalabs.todo.config.security.CustomUserDetailsService;
import com.sivalabs.todo.config.security.SecurityUser;
import com.sivalabs.todo.config.security.SecurityUtils;
import com.sivalabs.todo.config.security.TokenHelper;
import com.sivalabs.todo.entity.User;
import com.sivalabs.todo.model.AuthenticationRequest;
import com.sivalabs.todo.model.AuthenticationResponse;
import com.sivalabs.todo.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final TokenHelper tokenHelper;
    private final SecurityUtils securityUtils;
    private final ApplicationProperties applicationProperties;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    CustomUserDetailsService userDetailsService,
                                    TokenHelper tokenHelper,
                                    SecurityUtils securityUtils,
                                    ApplicationProperties applicationProperties) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenHelper = tokenHelper;
        this.securityUtils = securityUtils;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping(value = "/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername());
        return new AuthenticationResponse(jws, applicationProperties.getJwt().getExpiresIn());
    }

    @PostMapping(value = "/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            String email = tokenHelper.getUsernameFromToken(authToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Boolean validToken = tokenHelper.validateToken(authToken, userDetails);
            if (validToken) {
                String refreshedToken = tokenHelper.refreshToken(authToken);
                return ResponseEntity.ok(
                        new AuthenticationResponse(
                                refreshedToken,
                                applicationProperties.getJwt().getExpiresIn()
                        )
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> me() {
        User loginUser = securityUtils.loginUser();
        if(loginUser != null) {
            return ResponseEntity.ok(UserDTO.fromEntity(loginUser));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
