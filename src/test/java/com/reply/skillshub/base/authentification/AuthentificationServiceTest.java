package com.reply.skillshub.base.authentification;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.reply.skillshub.base.exceptionhandling.exeptions.UnconfirmedUser;
import com.reply.skillshub.base.exceptionhandling.exeptions.UserNotFound;
import com.reply.skillshub.base.services.EmailService;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.openapi.model.LoginRequest;

@ExtendWith(MockitoExtension.class)
public class AuthentificationServiceTest {

    @InjectMocks
    private AuthentificationService authentificationService;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Test
    void userNotFound_authentificationService() {
        doReturn(Optional.empty()).when(userService).findBaseUserByEmail(anyString());
        Assertions.assertThrows(UserNotFound.class, () -> authentificationService.loginUser(Instancio.create(LoginRequest.class)));
    }

    @Test
    void unconfirmedUser_authentificationService() {
        User user = Instancio.create(User.class);
        user.setConfirmed(false);
        doReturn(Optional.of(user)).when(userService).findBaseUserByEmail(anyString());
        Assertions.assertThrows(UnconfirmedUser.class, () -> authentificationService.loginUser(Instancio.create(LoginRequest.class)));
    }


    
}
