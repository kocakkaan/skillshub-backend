package com.reply.skillshub.base.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.reply.skillshub.data.userrole.UserRole;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockUser mockUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Set<GrantedAuthority> authorities = List.of(UserRole.ADMIN)
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());

        var user = new org.springframework.security.core.userdetails.User(mockUser.username(),
                "test",
                authorities);

        Authentication auth = new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities());
        context.setAuthentication(auth);
        return context; 
    }
    
}
