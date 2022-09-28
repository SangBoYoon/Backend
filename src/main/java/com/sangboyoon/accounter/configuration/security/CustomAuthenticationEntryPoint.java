package com.sangboyoon.accounter.configuration.security;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception != null && exception.equals("ExpiredJwtException")) {
            response.sendRedirect("/exception/expired");
        } else response.sendRedirect("/exception/entryPoint");
    }
}