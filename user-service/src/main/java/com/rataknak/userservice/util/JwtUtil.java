package com.rataknak.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME = 86400000; // 24 hours

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        try{
            return BCrypt.checkpw(password, hashedPassword);
        } catch( IllegalArgumentException e){
            return false;
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(this.SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String generateVerifiedEmailToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + VERIFIED_EMAIL_TOKEN_VALID_DURATION_MINUTES * 60 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim("emailVerified", true)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateOtpToken(String email, String otp) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TEMP_TOKEN_VALID_DURATION_MINUTES * 60 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim("otp", otp)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    private static final long VERIFIED_EMAIL_TOKEN_VALID_DURATION_MINUTES = 5; // 5 minutes for the verified email token
    private static final long TEMP_TOKEN_VALID_DURATION_MINUTES = 15; // 15 minutes for the temporary registration token
}