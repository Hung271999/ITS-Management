package com.sharp.vn.its.management.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sharp.vn.its.management.security.UserSecurityDetails;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

/**
 * The type Jwt utils.
 */
@Component
@Slf4j
public class JwtUtil {

    /**
     * The Jwt secret.
     */
    @Value("${its.app.jwtSecret}")
    private String jwtSecret;

    /**
     * The Jwt expiration ms.
     */
    @Value("${its.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * The Jwt cookie.
     */
    @Value("${its.app.jwtCookieName}")
    private String jwtCookie;

    /**
     * The Key.
     */
    private Key key;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Do generate token string.
     *
     * @param claims the claims
     * @param username the username
     * @return the string
     */
    private String doGenerateToken(Map<String, Object> claims, String username) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /**
     * Validate jwt token boolean.
     *
     * @param authToken the auth token
     * @return the boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            if (StringUtils.isEmpty(authToken)) {
                return false;
            }
            Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Generate token string.
     *
     * @param user the user
     * @return the string
     */
    public String generateToken(UserSecurityDetails user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = user.getAuthorities().stream()
                .map((GrantedAuthority item) -> item.getAuthority())
                .collect(Collectors.toList());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", roles);
        return doGenerateToken(claims, user.getUsername());
    }

    /**
     * Gets all claims from token.
     *
     * @param token the token
     * @return the all claims from token
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Gets username from token.
     *
     * @param token the token
     * @return the username from token
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Gets expiration date from token.
     *
     * @param token the token
     * @return the expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * Is token expired boolean.
     *
     * @param token the token
     * @return the boolean
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
