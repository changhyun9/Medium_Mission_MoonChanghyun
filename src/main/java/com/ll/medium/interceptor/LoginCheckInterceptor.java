package com.ll.medium.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final String LOGIN_MEMBER = "LOGIN_MEMBER";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {

            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
