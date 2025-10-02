package emt.emtlab.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * This class handles unauthorized requests - it will be called when a user tries to access
 * a protected resource without authentication
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    @Serial
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        // Log the attempted request path for security monitoring
        logger.warn("Unauthorized access attempt to: {}", request.getRequestURI());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"You need to login first to access this resource\"}");
    }
}