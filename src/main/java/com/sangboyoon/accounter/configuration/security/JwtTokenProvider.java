package com.sangboyoon.accounter.configuration.security;

import com.sangboyoon.accounter.domain.security.exceptions.CAuthenticationEntryPointException;
import com.sangboyoon.accounter.web.users.sign.dto.TokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.impl.Base64UrlCodec;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private String ROLES = "roles";
    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L;
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64UrlCodec.BASE64URL.encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public TokenDto createTokenDto(Long userPk, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLES, role);

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMillisecond)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if(claims.get(ROLES) == null) {
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        return null;
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("????????? Jwt ???????????????.");
        } catch (ExpiredJwtException e) {
            log.error("????????? ???????????????.");
        } catch (UnsupportedJwtException e) {
            log.error("???????????? ?????? ???????????????.");
        } catch (IllegalArgumentException e) {
            log.error("????????? ???????????????.");
        }

        return false;
    }

    public boolean validationToken(ServletRequest request, String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.error("????????? Jwt ???????????????.");
        } catch (MalformedJwtException e) {
            log.error("????????? Jwt ???????????????.");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
            log.error("????????? ???????????????.");
        } catch (UnsupportedJwtException e) {
            log.error("???????????? ?????? ???????????????.");
        } catch (IllegalArgumentException e) {
            log.error("????????? ???????????????.");
        }
        return false;
    }

    public Long getExpiration(String accessToken) {
        // accessToken ?????? ????????????
        Date expiration = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().getExpiration();
        // ?????? ??????
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public Long getRefreshTokenValidMillisecond() {
        return this.refreshTokenValidMillisecond;
    }
}
