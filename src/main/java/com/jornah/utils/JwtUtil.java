package com.jornah.utils;

import com.jornah.constant.ExceptionType;
import com.jornah.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author licong
 * @date 2021/10/2 20:30
 */
@Component
public final class JwtUtil implements InitializingBean {
    @Value("${jwt.key:L/Udm6rF1gnCpHdY8mN4nIavHjZhsEu9SWB9pCnHuyg=}")
    private String SECRET_KEY;
    private SecretKey key;

    public static JwtUtil getSingleton() {
        return SpringContextHolder.getBean(JwtUtil.class);
    }

    public String generateToken(Map<String, Object> payload, Instant expiration) {
        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(expiration.toEpochMilli()))
                .signWith(key)
                .compact();

    }

    public String generateToken(String payload, Instant expiration) {
        return Jwts.builder()
                .setPayload(payload)
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(expiration.toEpochMilli()))
                .signWith(key)
                .compact();

    }


    public Claims parseToken(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw BusinessException.of(ExceptionType.TOKEN_EXPIRED, "token 已过期");
        } catch (Throwable e) {
            throw BusinessException.of(ExceptionType.NOT_LOGIN, "请登录");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
