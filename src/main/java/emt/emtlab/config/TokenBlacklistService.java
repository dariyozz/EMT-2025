package emt.emtlab.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage blacklisted/revoked tokens.
 * In a production environment, consider using Redis or another distributed cache
 * instead of in-memory storage, especially for scalable applications.
 */
@Service
public class TokenBlacklistService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    @Value("${jwt.access.expiration}")
    private Long tokenExpiration;

    /**
     * Blacklist a token by adding it to the revocation list
     * @param token The JWT token to blacklist
     * @param expiryTime The token's expiration timestamp
     */
    public void blacklistToken(String token, Long expiryTime) {
        blacklistedTokens.put(token, expiryTime);

        // Clean up expired tokens from the blacklist (optional, can be scheduled)
        cleanupExpiredTokens();
    }

    /**
     * Check if a token is blacklisted
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    /**
     * Remove expired tokens from the blacklist to prevent memory leaks
     * In production, this should be a scheduled task.
     */
    private void cleanupExpiredTokens() {
        long currentTimeMillis = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTimeMillis);
    }
}