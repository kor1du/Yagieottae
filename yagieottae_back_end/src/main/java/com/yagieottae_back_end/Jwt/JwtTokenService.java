package com.yagieottae_back_end.Jwt;

import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenService
{
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final Key key;

//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 5000L; //5초

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L; //1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    /**
     * 기능: 토큰을 디코딩할 키 객체 생성
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값:
     **/
    public JwtTokenService(@Value("${jwt.secret}") String secretKey)
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 기능: 유저 정보를 토대로 Token들을 생성한다.
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 토큰
     **/
    public JwtTokenDto generateToken(Authentication authentication)
    {
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date accessTokenExpireMs = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireMs = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpireMs)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(refreshTokenExpireMs)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDto
                .builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireMs(accessTokenExpireMs)
                .refreshTokenExpireMs(refreshTokenExpireMs)
                .build();
    }

    /**
     * 기능: 토큰을 파싱한다.
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 파싱된 토큰의 정보
     **/
    public Claims parseClaims(String accessToken)
    {
        try
        {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e)
        {
            return e.getClaims();
        }
    }

    /**
     * 기능: 토큰의 권한 정보를 반환한다.
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 토큰 권한 정보
     **/
    public Authentication getAuthentication(String accessToken)
    {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null)
        {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims
                        .get("auth")
                        .toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 기능: 토큰의 유효시간 반환(AccessToken, RefreshToken 종류 무관)
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 토큰의 유효시간
     **/
    public Long getTokenExpirationDate(String token)
    {
        Claims claims = parseClaims(token);

        if (claims == null)
        {
            throw new CustomBadRequestException("해당 토큰이 존재하지 않습니다.");
        }

        return Long.parseLong(claims
                .get("exp")
                .toString());
    }

    public boolean validateToken(String token)
    {
        try
        {
            String redisToken = redisTemplate
                    .opsForValue()
                    .get(token); //redis에 accessToken과 일치하는 로그아웃된 토큰이 있는지 확인한다.
            if (redisToken != null && redisToken.equals("logout")) //이미 로그아웃된 토큰이라면
            {
                throw new JwtException("로그아웃된 토큰입니다!");
            }
            Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e)
        {
            log.info("Invalid JWT Token", e);
            throw new JwtException("잘못된 JWT 시그니처");
        } catch (ExpiredJwtException e)
        {
            log.info("Expired JWT Token", e);
            throw new JwtException("만료된 JWT 토큰");
        } catch (UnsupportedJwtException e)
        {
            log.info("Unsupported JWT Token", e);
            throw new JwtException("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException e)
        {
            log.info("JWT claims string is empty", e);
            throw new JwtException("값이 없음");
        }
    }
}
