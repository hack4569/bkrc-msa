package com.example.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class GatewayMemberAuthenticationFilter extends OncePerRequestFilter {

    public static final String MEMBER_ID_HEADER = "X-Member-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String memberIdHeader = request.getHeader(MEMBER_ID_HEADER);

        if (memberIdHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Long memberId = Long.valueOf(memberIdHeader);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(memberId, null, List.of()));
            } catch (NumberFormatException exception) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
