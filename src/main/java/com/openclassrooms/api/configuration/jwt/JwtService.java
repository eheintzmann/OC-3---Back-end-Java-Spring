package com.openclassrooms.api.configuration.jwt;

import com.openclassrooms.api.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Jwt service
 */
@Slf4j
@Service
public class JwtService {
    private final Duration expireDuration;
    private final String issuer;
    private final SecretKey secretKey;

    /**
     * Constructor for JwtService class
     *
     * @param appJwtSecret String
     * @param appJwtSalt String
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public JwtService(
            @Value("${app.jwt.secret}") String appJwtSecret,
            @Value("${app.jwt.salt}") String appJwtSalt,
            @Value("${app.jwt.expiration}") String appJwtExpiration,
            @Value("${app.jwt.issuer}") String appJwtIssuer
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.secretKey = getKeyFromPassword(appJwtSecret, appJwtSalt);
        this.expireDuration = Duration.of(Long.parseLong(appJwtExpiration), ChronoUnit.HOURS);
        this.issuer = appJwtIssuer;
    }

    /**
     * Create a JSON Web Token
     *
     * @param user User
     * @return Access Token
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                // Subject is combination of the user’s ID and email, separated by a comma
                .subject(String.format("%s,%s", user.getId(), user.getEmail()))
                // Issuer name
                .issuer(issuer)
                // Token is issued at the current date and time
                .issuedAt(Date.from(Instant.now()))
                // Token expiration
                .expiration(Date.from(Instant.now().plus(expireDuration)))
                // Token is signed using a secret key. Signature algorithm is HMAC using SHA-512.
                .signWith(secretKey, Jwts.SIG.HS512)
                // Compact token into its final String form.
                .compact();
    }

    /**
     * Verify a given JSON Web Token
     *
     * @param token String
     * @return boolean
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired ! " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Token is invalid ! " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported ! " + ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Signature validation failed ! " + ex.getMessage());
        } catch (IncorrectClaimException ex) {
            log.error("Incorrect claimn ! " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace ! " + ex.getMessage());
        }

        return false;
    }

    /**
     * Gets the value of the subject field of a given token.
     * Subject contains User ID and email
     *
     * @param token String
     * @return String
     */
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Parse JWT for information (claims)
     *
     * @param token String
     * @return Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     *
     * @param password String
     * @param salt String
     * @return SecretKey
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    private static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 512);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "HmacSHA512");
    }
}
