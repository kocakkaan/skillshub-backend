package com.reply.skillshub.base.authentification;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reply.skillshub.base.exceptionhandling.exeptions.InvalidConfirmationToken;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.data.EmailRequest;
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
        		Authentication authenticationRequest =
			UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getAccessCode());
		Authentication authenticationResponse =
			this.authenticationManager.authenticate(authenticationRequest);
        var principal = (org.springframework.security.core.userdetails.User) authenticationResponse.getPrincipal();

        User user = userService.findUserByEmail(principal.getUsername()).get();
        var jwt = jwtService.createJwtToken(principal.getUsername());

        SecurityContextHolder.getContext().setAuthentication(new RememberMeAuthenticationToken(jwt, principal, principal.getAuthorities()));
        
        return new LoginResponse()
                        .id(user.getId())
                        .jwt(jwt)
                        .role(user.getUserRole().name());
    }



    public void signUpUser(SignupRequest signupRequest) {
        if (!signupRequest.getAccessCode().equals(signupRequest.getAccessCodeConfirmed())) {
            throw new ValidationException("Passwords must match");
        }

        User newUser = convertSignupRequestToUser(signupRequest);

        newUser.setConfirmationToken(UUID.randomUUID().toString());

        User savedUser = userService.save(newUser);

        emailService.sendEmail(createEmailConfirmationRequest(savedUser));
    }

    public void confirmUser(String token) {

        Optional<User> potentialUser = userService.findUserByConfirmationToken(token);

        if (potentialUser.isPresent()) {
            User user = potentialUser.get();
            user.setConfirmed(true);
            userService.save(user);
        } else {
            throw new InvalidConfirmationToken("The user could not be identified");
        }
        
    }

    private EmailRequest createEmailConfirmationRequest(User user) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRecipient(user.getEmail());
        emailRequest.setSubject("Please verify your email address for your account");
        emailRequest.setMessage(getEmailBody(user));
        return emailRequest;
    }

    private String getEmailBody(User user) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Please verify your email address for your account");
        emailBody.append("\n\n");
        emailBody.append("Thank you for signing up. To ensure the security of your account, we need to verify your email address.");
        emailBody.append("\n\n");
        emailBody.append("Please click on the following link:");
        emailBody.append("\n\n");
        emailBody.append(getConfirmationLink(user));
        emailBody.append("\n\n");
        emailBody.append("If you did not request this, you can ignore this email. Someone else might have entered your email address by mistake.");
        emailBody.append("\n\n");
        emailBody.append("Thank you");
        return emailBody.toString();
    }

    private String getConfirmationLink(User user) {
        StringBuilder emailLink = new StringBuilder();
        emailLink.append(server);
        emailLink.append("/auth/confirmation/");
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
