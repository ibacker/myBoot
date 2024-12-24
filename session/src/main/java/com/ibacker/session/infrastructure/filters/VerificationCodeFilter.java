package com.ibacker.session.infrastructure.filters;

import com.ibacker.session.infrastructure.config.VerificationConfig;
import com.ibacker.session.infrastructure.exception.VerificationCodeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Order(Integer.MAX_VALUE - 1)
@Component
public class VerificationCodeFilter extends OncePerRequestFilter implements CommandLineRunner, InitializingBean {

    @Resource
    private VerificationConfig verificationConfig;


    @Override
    protected void initFilterBean() throws ServletException {
        System.out.println("Filter初始化...");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        FilterConfig filterConfig = super.getFilterConfig();
        ServletContext servletContext = super.getServletContext();
        Environment environment = super.getEnvironment();

        String requestURI = request.getRequestURI();

        if (verificationConfig.getCheckList().contains(requestURI)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("verifyCode") == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "验证码未找到");
                return;
            }

            String providedCode = request.getParameter("verifyCode");
            String sessionCode = (String) session.getAttribute("verifyCode");

            if (!sessionCode.equals(providedCode)) {
                throw new VerificationCodeMismatchException("验证码错误");
            }
        }

        filterChain.doFilter(request, response);

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Filter初始化 CommandLineRunner ...");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Filter初始化 InitializingBean do something");
    }
}
