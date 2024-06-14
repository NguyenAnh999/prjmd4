package com.ra.securerity.JWT;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt_expiration}")
    private int expiration;
    @Value("${jwt_secret}")
    private String secret;

    public String createToken(UserDetails userDetails){
        Date today = new Date();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(today)
                .setExpiration(new Date(today.getTime()+expiration))
                .signWith(SignatureAlgorithm.HS384,secret)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
              log.error("Da het han");
        }catch (UnsupportedJwtException e){
            log.error("Server khong ho tro bao mat voi jwt");
        }catch (MalformedJwtException e){
            log.error("Chuoi token khong dung");
        }catch (SignatureException e){
            log.error("Chu ki khong dung");
        }catch (IllegalArgumentException e){
            log.error("Tham so khong dung");
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
