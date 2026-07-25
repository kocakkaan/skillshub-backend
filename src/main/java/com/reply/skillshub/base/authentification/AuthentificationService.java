package com.reply.skillshub.base.authentification;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

import com.reply.skillshub.base.exceptionhandling.exeptions.UnconfirmedUser;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.data.EmailRequest;
import com.reply.skillshub.data.user.BaseUser;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.data.userrole.UserRole;
import com.reply.skillshub.openapi.model.LoginRequest;
import com.reply.skillshub.openapi.model.LoginResponse;
import com.reply.skillshub.openapi.model.SignupRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationService {

    @Value("${skillhub.server}")
    private String server;

    private final UserService userService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponse loginUser(LoginRequest loginRequest) {
        BaseUser user = userService.findBaseUserByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFound());

        if (!user.getConfirmed()) {
            throw new UnconfirmedUser("A user must be confirmed to login. Please confirm your account");
        }

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.getEmail(), loginRequest.getAccessCode());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        var principal = (org.springframework.security.core.userdetails.User) authenticationResponse.getPrincipal();

        var jwt = jwtService.createJwtToken(principal.getUsername());

        SecurityContextHolder.getContext()
                .setAuthentication(new RememberMeAuthenticationToken(jwt, principal, principal.getAuthorities()));

        return new LoginResponse()
                .id(user.getId())
                .jwt(jwt)
                .role(user.getUserRole().name());
    }

    public void signUpUser(SignupRequest signupRequest) {
        if (!signupRequest.getAccessCode().equals(signupRequest.getAccessCodeConfirmed())) {
            throw new ValidationException("Passwords must match");
        }

        // Resolve the super/root admin user that will act as the creator of every
        // self-registered user.  A super user must be pre-seeded in Neo4j (e.g. via
        // Cypher) before the signup endpoint can be used.
        User creator = userService.findFirstByUserRole(UserRole.ADMIN)
                .orElseThrow(() -> new UserNotFound(
                        "No ADMIN user found. A super user must be seeded in the database before signup."));

        User newUser = convertSignupRequestToUser(signupRequest);
        newUser.setConfirmationToken(UUID.randomUUID().toString());
        newUser.setCreatedBy(creator);
        newUser.setCreatedUserId(creator.getId());
        newUser.setCreatedOn(LocalDate.now());

        User savedUser = userService.save(newUser);

        emailService.sendEmail(createEmailConfirmationRequest(savedUser));
    }

    public void confirmUser(String token) {

        var potentialUser = userService.findUserByConfirmationToken(token, UserToConfirm.class);

        if (potentialUser.isPresent()) {
            var user = potentialUser.get();
            user.setConfirmed(true);
            userService.save(user);
        } else {
            throw new UnconfirmedUser("The user could not be identified");
        }

    }

    private EmailRequest createEmailConfirmationRequest(User user) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRecipient(user.getEmail());
        emailRequest.setSubject("Please verify your email address for your account");
        emailRequest.setTemplate("confirmation");
        Context context = new Context();
        context.setVariable("link", getConfirmationLink(user));
        context.setVariable("username", user.getFullname());
        emailRequest.setContext(context);
        return emailRequest;
    }

    private String getConfirmationLink(User user) {
        StringBuilder emailLink = new StringBuilder();
        emailLink.append(server);
        emailLink.append("/api/auth/confirmation/");
        emailLink.append(user.getConfirmationToken());
        return emailLink.toString();
    }

    private User convertSignupRequestToUser(SignupRequest signupRequest) {
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getAccessCode()));
        user.setUserRole(UserRole.ADMIN);
        return user;
    }
}
