package com.example.schedule.global.common.filter;


import com.example.schedule.global.common.exception.ErrorDetail;
import com.example.schedule.global.common.response.ErrorResponse;
import com.example.schedule.user.exception.UserNotLoginException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import static com.example.schedule.global.common.SessionConst.LOGIN_MEMBER;
import static com.example.schedule.global.common.exception.ErrorCode.*;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class LoginFilter extends OncePerRequestFilter {

    private static final String USER_PATH = "/api/v1/users";
    private static final String[] WHITE_LIST = {USER_PATH + "/register", USER_PATH + "/login"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        try {
            log.info("REQUEST: [{} {}]", request.getMethod(), request.getRequestURI());
            if (isLoginCheckPath(requestURI)) {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
                    throw new UserNotLoginException(List.of(
                        new ErrorDetail(UN_AUTHORIZED, null, "로그인이 필요한 서비스입니다. 로그인을 해주세요."))
                    );
                }
            }
            filterChain.doFilter(request, response);
        } catch (UserNotLoginException unle) {
            log.error("Authentication failed: {}", unle.getErrorDetails());
            setResponseStatusAndHeader(response);

            List<ErrorDetail> errorDetails = unle.getErrorDetails();
            ErrorResponse errorResponse = ErrorResponse.fail(UNAUTHORIZED, errorDetails);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
        log.info("RESPONSE: [{} {}]", request.getMethod(), request.getRequestURI());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.asList(WHITE_LIST).contains(requestURI);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private void setResponseStatusAndHeader(HttpServletResponse response) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
