package com.reply.skillshub.base.authentification;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.reply.skillshub.base.exceptionhandling.SkillhubExceptionHandler;
import com.reply.skillshub.openapi.model.ErrorDto;
import com.reply.skillshub.openapi.model.SignupRequest;

@WebMvcTest(AuthentificationController.class)
@ContextConfiguration(classes = {AuthentificationController.class, SkillhubExceptionHandler.class})
@AutoConfigureMockMvc(addFilters=false)
public class AuthentificationControllerTest {

    @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthentificationService service;

    @Test
    public void test_valid_signup_request() {
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setAccessCodeConfirmed(signupRequest.getAccessCode());

        given()
            .mockMvc(mockMvc)
            .body(signupRequest)
            .contentType("application/json")
            .when().post("/auth/signup")
            .then().assertThat().statusCode(200);
    }

    @ParameterizedTest
    @MethodSource("requiredParams")
    public void test_invalid_signup_request(SignupRequest signupRequest) {

        var errorMessage = given()
            .mockMvc(mockMvc)
            .body(signupRequest)
            .contentType("application/json")
            .when().post("/auth/signup")
            .as(ErrorDto.class);
        
        Assertions.assertEquals(1, errorMessage.getDetails().size());
    }

    private static Stream<Arguments> requiredParams() {
        return Stream.of(
            Arguments.of(Instancio.create(SignupRequest.class).accessCode(null)),
            Arguments.of(Instancio.create(SignupRequest.class).accessCodeConfirmed(null)),
            Arguments.of(Instancio.create(SignupRequest.class).email(null)),
            Arguments.of(Instancio.create(SignupRequest.class).firstName(null)),
            Arguments.of(Instancio.create(SignupRequest.class).lastName(null))
        );
    }
}
