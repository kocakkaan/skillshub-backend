package com.reply.skillshub.base.configuration;

import java.io.IOException;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.reply.skillshub.base.authentification.JwtService;
import com.reply.skillshub.base.services.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
   private final CustomUserDetailsService userDetailsService;
   private final JwtService jwtService;

   @Override
   protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
      throws ServletException, IOException {

        String jwt = parseJwtHeader(request);

        if (jwt != null && jwtService.validateJwtToken(jwt)) {
            setSecurityConceptFromToken(jwt);
        }
        filterChain.doFilter(request, response);
   }

   private void setSecurityConceptFromToken(String jwt) {
        var userEmail = jwtService.getSubjectFromToken(jwt);
        var userDetails = userDetailsService.loadUserByUsername(userEmail);
        SecurityContextHolder.getContext().setAuthentication(new RememberMeAuthenticationToken(jwt, userDetails, userDetails.getAuthorities()));
   }

   private String parseJwtHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7); // remove bearer
        }
        return null;
   }
    
}
