package com.oa.common.utils;

import com.oa.module.system.service.ConfigService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 密钥和过期时间从数据库配置读取
 */
@Component
public class JwtUtil {
    
    // 默认值（数据库配置不可用时的备用）
    private static final String DEFAULT_SECRET_KEY = "oa_default_secret_key_please_change_in_database";
    private static final long DEFAULT_EXPIRATION_TIME = 86400000L; // 24 hours
    
    @Autowired(required = false)
    private ConfigService configService;
    
    /**
     * 获取JWT密钥（从数据库配置）
     */
    private String getSecretKey() {
        if (configService != null) {
            return configService.getValue("jwt.secret", DEFAULT_SECRET_KEY);
        }
        return DEFAULT_SECRET_KEY;
    }
    
    /**
     * 获取JWT过期时间（从数据库配置）
     */
    private long getExpirationTime() {
        if (configService != null) {
            return configService.getLongValue("jwt.expiration", DEFAULT_EXPIRATION_TIME);
        }
        return DEFAULT_EXPIRATION_TIME;
    }

    public String generateToken(String username, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}