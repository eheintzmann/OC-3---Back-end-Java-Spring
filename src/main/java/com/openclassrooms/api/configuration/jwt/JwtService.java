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
import java.util.Date;

/**
 * Jwt service
 */
@Slf4j
@Service
public class JwtService {
    private static final Duration EXPIRE_DURATION = Duration.parse("PT24H");
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
            @Value("${app.jwt.salt}") String appJwtSalt
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.secretKey = getKeyFromPassword(appJwtSecret, appJwtSalt);
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
                // Issuer name is OCRentalAPI
                .issuer("OCRentalAPI")
                // Token is issued at the current date and time
                .issuedAt(Date.from(Instant.now()))
                // Token expires after 24 hours
                .expiration(Date.from(Instant.now().plus(EXPIRE_DURATION)))
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
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired !" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace !" + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Token is invalid !" + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported !" + ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Signature validation failed !" + ex.getMessage());
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
