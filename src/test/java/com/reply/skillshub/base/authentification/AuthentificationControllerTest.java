package com.reply.skillshub.base.authentification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;

import com.reply.skillshub.base.exceptionhandling.SkillhubExceptionHandler;
import com.reply.skillshub.openapi.model.Error;
import com.reply.skillshub.openapi.model.ErrorDetail;
import com.reply.skillshub.openapi.model.SignUpRequest;

@WebMvcTest(AuthentificationController.class)
@ContextConfiguration(classes = {AuthentificationController.class, SkillhubExceptionHandler.class})
@AutoConfigureMockMvc(addFilters=false)
public class AuthentificationControllerTest {

    @Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthentificationServiceTest service;

    @Test
    public void test_valid_signup_request() {
        SignUpRequest signUpRequest = Instancio.create(SignUpRequest.class);
        signUpRequest.setAccessCodeConfirmed(signUpRequest.getAccessCode());

        given()
            .mockMvc(mockMvc)
            .body(signUpRequest)
            .contentType("application/json")
            .when().post("/auth/signup")
            .then().assertThat().statusCode(200);
    }

    @ParameterizedTest
    @MethodSource("requiredParams")
    public void test_invalid_signup_request(SignUpRequest signUpRequest) {

        var errorMessage = given()
            .mockMvc(mockMvc)
            .body(signUpRequest)
            .contentType("application/json")
            .when().post("/auth/signup")
            .as(Error.class);
        
        Assertions.assertEquals(1, errorMessage.getDetails().size());
    }

    private static Stream<Arguments> requiredParams() {
        return Stream.of(
            Arguments.of(Instancio.create(SignUpRequest.class).accessCode(null)),
            Arguments.of(Instancio.create(SignUpRequest.class).accessCodeConfirmed(null)),
            Arguments.of(Instancio.create(SignUpRequest.class).email(null)),
            Arguments.of(Instancio.create(SignUpRequest.class).firstName(null)),
            Arguments.of(Instancio.create(SignUpRequest.class).lastName(null))
        );
    }
}
