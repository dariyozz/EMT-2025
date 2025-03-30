package emt.emtlab.controller;

import emt.emtlab.exceptions.InvalidUserCredentialsException;
import emt.emtlab.services.application.dto.auth.DisplayUserDto;
import emt.emtlab.services.application.dto.auth.LoginRequest;
import emt.emtlab.services.application.dto.auth.RegisterRequest;
import emt.emtlab.services.application.service.AuthApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration")
public class AuthController {

    private final AuthApplicationService authService;
    private final AuthenticationManager authManager;

    public AuthController(AuthApplicationService authService, AuthenticationManager authManager) {
        this.authService = authService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register a new user account")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully!");
    }


    @Operation(summary = "User login", description = "Authenticates a user and starts a session")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully"
            ), @ApiResponse(responseCode = "404", description = "Invalid username or password")}
    )
    @PostMapping("/login")
    public ResponseEntity<DisplayUserDto> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            DisplayUserDto displayUserDto = authService.login(
                    loginRequest).orElseThrow(InvalidUserCredentialsException::new);

            request.getSession().setAttribute("user", displayUserDto.toUser());
            return ResponseEntity.ok(displayUserDto);
        } catch (InvalidUserCredentialsException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "User logout", description = "Ends the user's session")
    @ApiResponse(responseCode = "200", description = "User logged out successfully")
    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }


    @GetMapping("/current-user")
    @Operation(summary = "Get Current User", description = "Returns information about the currently authenticated user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication.getName());
    }

    @GetMapping("/login-failure")
    @Operation(summary = "Login Failure", description = "Endpoint for handling login failures")
    public ResponseEntity<?> loginFailure() {
        return ResponseEntity.status(401).body("Login failed: Invalid credentials");
    }
}