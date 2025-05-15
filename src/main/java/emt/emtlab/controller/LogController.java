package emt.emtlab.controller;

import emt.emtlab.exceptions.ApiErrorResponse;
import emt.emtlab.services.domain.model.LoggedInUserLogs;
import emt.emtlab.services.domain.repository.LoggedInUserInfoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "API for system logs and monitoring")
@RequiredArgsConstructor
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    private final LoggedInUserInfoRepository loggedInUserInfoRepository;

    @GetMapping("/login-info")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get All Login Information", description = "Returns information about all logged-in users (Admin only)")
    public ResponseEntity<?> getLoginInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                List<LoggedInUserLogs> allLoginInfo = loggedInUserInfoRepository.findAll();

                List<Map<String, Object>> result = allLoginInfo.stream()
                        .map(loginInfo -> {
                            Map<String, Object> info = new HashMap<>();
                            info.put("username", loginInfo.getUser().getUsername());
                            info.put("email", loginInfo.getUser().getEmail());
                            info.put("loginTime", loginInfo.getLoginTime());
                            info.put("tokenExpiresAt", new Date(loginInfo.getTokenExpirationTime()));
                            info.put("JWT Token", loginInfo.getJWT_token());
                            return info;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(result);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponse("Not authenticated"));
        } catch (Exception e) {
            logger.error("Error retrieving login information: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse("Error retrieving login information"));
        }
    }
    @GetMapping("/my-roles")
    public ResponseEntity<?> getMyRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> info = new HashMap<>();
        info.put("username", auth.getName());
        info.put("isAuthenticated", auth.isAuthenticated());
        info.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(info);
    }
}