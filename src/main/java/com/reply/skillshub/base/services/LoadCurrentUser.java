package com.reply.skillshub.base.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.data.user.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;

@Service
@RequiredArgsConstructor
public class LoadCurrentUser {

    private final UserRepository userRepository;

    public User loadContextUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public com.reply.skillshub.data.user.User loadSkillhubUserFromContext() {
        return userRepository.findByEmail(loadContextUser().getUsername()).orElseThrow(() -> new UserNotFound());
    }
}
