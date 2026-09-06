package ru.ekaterina.gateway.controller;

import jakarta.validation.Valid;
import ru.ekaterina.gateway.dto.AuthResponse;
import ru.ekaterina.gateway.dto.LoginRequest;
import ru.ekaterina.gateway.dto.RegisterRequest;
import ru.ekaterina.gateway.dto.UserDTO;
import ru.ekaterina.gateway.security.JwtTokenProvider;
import ru.ekaterina.gateway.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO dto = authService.registerNewUser(
                request.username(),
                request.password(),
                request.name(),
                request.email(),
                request.role()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = tokenProvider.createToken(auth);
        UserDTO dto = authService.findByUsername(request.username());

        return ResponseEntity.ok(new AuthResponse(token, dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
