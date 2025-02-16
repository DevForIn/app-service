package com.mooo.devforin.appservice.config.filter;

import com.mooo.devforin.appservice.config.xss.XssRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        XssRequestWrapper xssRequestWrapper = new XssRequestWrapper(httpServletRequest);
        chain.doFilter(xssRequestWrapper, response);

    }
}
