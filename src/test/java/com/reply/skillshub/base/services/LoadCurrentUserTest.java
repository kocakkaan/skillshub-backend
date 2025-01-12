package com.reply.skillshub.base.services;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.reply.skillshub.data.user.User;
import com.reply.skillshub.data.user.UserRepository;

@SpringBootTest(classes = LoadCurrentUser.class)
public class LoadCurrentUserTest {

    @Autowired
    private LoadCurrentUser loadCurrentUser;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void testLoadContextUser() {
        Assertions.assertThat(loadCurrentUser.loadContextUser().getUsername()).isEqualTo("test");
    }

    @Test
    @WithMockUser
    void testloadSkillhubUserFromContext() {
        var user = Instancio.create(User.class);
        user.setEmail("test");
        doReturn(Optional.of(user)).when(userRepository).findByEmail("test", User.class);
        Assertions.assertThat(loadCurrentUser.loadSkillhubUserFromContext().getEmail()).isEqualTo("test");
    }
}
