package com.capston.chatting.provider;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String secretKey = "secretKey";
    private static final long tokenValidMillis = 1000L * 60 * 60;

    // 이름으로 Jwt token 을 생성한다.
    public String generateToken(String name) {
        Date now = new Date();
        return Jwts.builder()
                .setId(name)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Jwt token 을 복호화 하여 이름을 얻는다.
    public String getUserNameFromJwt(String jwt) {
        return getClaims(jwt).getBody().getId();
    }

    // Jwt token 의 유효성을 체크한다.
    public boolean validateToken(String jwt) {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
            throw e;
        }
    }
}
