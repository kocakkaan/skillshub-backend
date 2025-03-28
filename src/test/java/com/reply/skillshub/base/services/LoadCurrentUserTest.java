package com.reply.skillshub.base.services;

import static org.instancio.Select.field;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.reply.skillshub.BaseUserImp;
import com.reply.skillshub.data.user.UserService;   

@SpringBootTest(classes = LoadCurrentUser.class)
public class LoadCurrentUserTest {

    @Autowired
    private LoadCurrentUser loadCurrentUser;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    void testLoadContextUser() {
        Assertions.assertThat(loadCurrentUser.loadContextUser().getUsername()).isEqualTo("test");
    }

    @Test
    @WithMockUser
    void testloadSkillhubUserFromContext() {
        var user = Instancio.of(BaseUserImp.class).set(field(BaseUserImp::getEmail), "test").create();
        doReturn(Optional.of(user)).when(userService).findBaseUserByEmail(user.getEmail());
        Assertions.assertThat(loadCurrentUser.loadSkillhubUserFromContext().getEmail()).isEqualTo(user.getEmail());
    }
}
