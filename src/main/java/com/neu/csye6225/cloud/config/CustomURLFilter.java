package com.neu.csye6225.cloud.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class CustomURLFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    if (!(requestURI.matches("/healthz") || requestURI.matches("/v1/user") || requestURI.matches("/v1/user/self"))) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    filterChain.doFilter(request, response);
  }

}
