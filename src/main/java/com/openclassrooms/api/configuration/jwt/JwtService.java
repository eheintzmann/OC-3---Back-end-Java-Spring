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

@Slf4j
@Service
public class JwtService {
    private static final Duration EXPIRE_DURATION = Duration.parse("PT24H");
    private final SecretKey secretKey;

    public JwtService(
            @Value("${app.jwt.secret}") String appJwtSecret,
            @Value("${app.jwt.salt}") String appJwtSalt
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.secretKey = getKeyFromPassword(appJwtSecret, appJwtSalt);
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(String.format("%s,%s", user.getId(), user.getEmail()))
                .issuer("OCRentalAPI")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(EXPIRE_DURATION)))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

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

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 512);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "HmacSHA512");
    }
}
