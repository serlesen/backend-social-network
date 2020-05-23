package com.sergio.socialnetwork.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.socialnetwork.dto.CredentialsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class UserAuthenticationFilter implements Filter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if ("/v1/signIn".equals(httpReq.getServletPath())
                && HttpMethod.POST.matches(httpReq.getMethod())) {
            CredentialsDto credentialsDto = MAPPER.readValue(httpReq.getInputStream(), CredentialsDto.class);

            securityContext.setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            credentialsDto.getLogin(), credentialsDto.getPassword()));
        } else {
            String header = httpReq.getHeader(HttpHeaders.AUTHORIZATION);

            if (header != null) {
                String[] authElements = header.split(" ");

                if (authElements.length == 2
                        && "Bearer".equals(authElements[0])) {
                    securityContext.setAuthentication(new PreAuthenticatedAuthenticationToken(authElements[1], null));
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
