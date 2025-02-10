package com.example.schedule.global.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class MdcLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String uuid = UUID.randomUUID().toString();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        MDC.put("request_id", uuid);
        MDC.put("http_method", httpRequest.getMethod());
        MDC.put("request_uri", httpRequest.getRequestURI());

        log.info("REQUEST: [{} {}]", httpRequest.getMethod(), httpRequest.getRequestURI());
        chain.doFilter(request, response);
        log.info("RESPONSE: [{} {}]", httpRequest.getMethod(), httpRequest.getRequestURI());
        MDC.clear();
    }
}
