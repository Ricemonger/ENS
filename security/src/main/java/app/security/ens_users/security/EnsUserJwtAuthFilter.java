package app.security.ens_users.security;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import app.security.ens_users.EnsUserDetails;
import app.security.ens_users.EnsUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class EnsUserJwtAuthFilter extends OncePerRequestFilter {

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    private final EnsUserDetailsService ensUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("No Bearer Token provided for EnsUserJwtAuthFilter");
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        String accountId;
        try {
            accountId = abstractUserJwtUtil.extractAccountId(token);
        } catch (JwtException e) {
            log.warn("JwtException occurred for token {}: {}", token, e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
        if (accountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            EnsUserDetails ensUserDetails = (EnsUserDetails) ensUserDetailsService.loadUserByAccountId(accountId);
            if (abstractUserJwtUtil.isTokenValid(token, ensUserDetails)) {
                log.trace("Token {} is valid, authorizing...", token);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(ensUserDetails, null, ensUserDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}