package com.reply.skillshub.base.authentification;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final Key key;

    JwtService() {
        key = getSigningKey();
    }
    
    public String createJwtToken(String subject) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2)); // two days
        String jsonToken = Jwts.builder()
                                    .setId(id)
                                    .setSubject(subject)
                                    .setIssuedAt(now)
                                    .setNotBefore(now)
                                    .setExpiration(exp)
                                    .signWith(key)
                                    .compact();

        return jsonToken;
    }

    public boolean validateJwtToken(String tokenToValidate) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenToValidate);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            LOG.error("JWT validate token failed", e);
            return false;
        }
    }
    
    public String getSubjectFromToken(String tokenToValidate) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenToValidate).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            LOG.error("JWT parse token failed", e);
            return null;
        }
    }

    SecretKey getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}
