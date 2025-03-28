package com.reply.skillshub.base.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoadCurrentUser {

    private final UserService userService;

    public User loadContextUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public com.reply.skillshub.data.user.BaseUser loadSkillhubUserFromContext() {
        return userService.findBaseUserByEmail(loadContextUser().getUsername()).orElseThrow(() -> new UserNotFound());
    }
}
