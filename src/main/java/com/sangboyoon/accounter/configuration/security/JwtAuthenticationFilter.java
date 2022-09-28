package com.sangboyoon.accounter.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    public JwtAuthenticationFilter(JwtTokenProvider jwtProvider, StringRedisTemplate redisTemplate) {
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = jwtProvider.resolveToken((HttpServletRequest) request);

        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if(accessToken != null && jwtProvider.validationToken(request, accessToken)) {
            log.info("value: " + redisTemplate.opsForValue().get(accessToken));
            String isLogout = redisTemplate.opsForValue().get(accessToken);
            if(ObjectUtils.isEmpty(isLogout)) {
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
