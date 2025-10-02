package emt.emtlab.controller;

import emt.emtlab.config.JwtUtil;
import emt.emtlab.config.TokenBlacklistService;
import emt.emtlab.exceptions.ApiErrorResponse;
import emt.emtlab.services.application.dto.auth.AuthenticationResponse;
import emt.emtlab.services.application.dto.auth.LoginRequest;
import emt.emtlab.services.application.dto.auth.RefreshTokenRequest;
import emt.emtlab.services.application.dto.auth.RegisterRequest;
import emt.emtlab.services.application.service.AuthApplicationService;
import emt.emtlab.services.domain.model.LoggedInUserLogs;
import emt.emtlab.services.domain.model.User;
import emt.emtlab.services.domain.repository.LoggedInUserInfoRepository;
import emt.emtlab.services.domain.service.UserDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthApplicationService authApplicationService;
    private final UserDomainService userDomainService;
    private final TokenBlacklistService tokenBlacklistService;
    private final LoggedInUserInfoRepository loggedInUserInfoRepository;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user credentials and returns JWT tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Account disabled")
    })
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginDto,
            HttpServletRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Get user details from the database and ensure it's managed by the persistence context
            User user = userDomainService.findByUsername(userDetails.getUsername());
            String email = user.getEmail();

            // Log successful login attempt
            logger.info("User {} logged in successfully from IP {}",
                    userDetails.getUsername(),
                    request.getRemoteAddr());

            Date expirationDate = jwtUtil.extractExpiration(accessToken);
        
            // Create and save login info
            LoggedInUserLogs loginInfo = new LoggedInUserLogs(user, accessToken, expirationDate.getTime());
        
            try {
                loggedInUserInfoRepository.save(loginInfo);
            } catch (Exception e) {
                logger.warn("Failed to save login information: {}", e.getMessage());
                // Continue with authentication even if logging fails
            }

            return ResponseEntity.ok(new AuthenticationResponse(
                    accessToken,
                    userDetails.getUsername(),
                    email
            ));
        } catch (DisabledException e) {
            logger.warn("User account is disabled: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiErrorResponse("Account is disabled"));
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {} from IP: {}",
                    loginDto.getUsername(),
                    request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse("Invalid credentials"));
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("Authentication failed"));
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Generate new access token using refresh token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            // Extract username from refresh token
            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = jwtUtil.extractUsername(refreshToken);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("Invalid refresh token"));
            }

            // Check if token is expired or blacklisted
            if (jwtUtil.isTokenExpired(refreshToken) || tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("Refresh token expired or invalid"));
            }

            // Generate new access token
            UserDetails userDetails = userDomainService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse("Token refresh failed"));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registrationDto) {
        try {
            authApplicationService.register(registrationDto);
            return ResponseEntity.ok().body(Map.of("message", "User registered successfully!"));
        } catch (IllegalArgumentException e) {
            // Handle validation errors like username exists, etc.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse("Registration failed"));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidates the user's tokens")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String username = jwtUtil.extractUsername(jwt);

                // Get token expiration date to optimize token blacklist storage
                Date expiryDate = jwtUtil.extractExpiration(jwt);

                // Add token to blacklist
                tokenBlacklistService.blacklistToken(jwt, expiryDate.getTime());

                logger.info("User {} logged out successfully", username);

                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            }

            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("Logout failed"));
        }
    }

    @GetMapping("/current-user")
    @Operation(summary = "Get Current User", description = "Returns information about the currently authenticated user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                User user = (User) userDomainService.loadUserByUsername(username);

                // Return user information (customize as needed)
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", user.getUsername());
                userInfo.put("email", user.getEmail());
                userInfo.put("roles", authentication.getAuthorities());

                return ResponseEntity.ok(userInfo);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse("Not authenticated"));
        } catch (Exception e) {
            logger.error("Error retrieving current user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("Error retrieving user information"));
        }
    }

}