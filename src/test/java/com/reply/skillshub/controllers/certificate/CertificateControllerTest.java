package com.reply.skillshub.controllers.certificate;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.reply.skillshub.base.exceptionhandling.SkillhubExceptionHandler;
import com.reply.skillshub.data.hascertificate.HasCertificate;
import com.reply.skillshub.openapi.model.CertificateDto;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.response.MockMvcResponse;

@WebMvcTest(CertificateController.class)
@ContextConfiguration(classes = {CertificateController.class, SkillhubExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
public class CertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CertificateControllerService certificateControllerService;

    @Test
    public void shouldReturnAlistWithResponse200WhenServiceReturnsList() {

        doReturn(Instancio.createList(HasCertificate.class)).when(certificateControllerService).findAllByUserId(any(String.class));

        MockMvcResponse response = given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .when().get("/users/{userId}/certificates", "any");

        response.then().assertThat().statusCode(200);

        Assertions.assertNotSame(0, response.as(CertificateDto[].class).length);

    }

    // @Test
    // public void shouldReturn404() {
    //     CertificateDto certificateDto = Instancio.create(CertificateDto.class);

    //     doThrow(UserNotFound.class).when(certificateControllerService).saveCertificateForUser(any(String.class), any(CertificateDto.class));

    //     MockMvcResponse response = given()
    //             .mockMvc(mockMvc)
    //             .contentType(ContentType.JSON)
    //             .body(certificateDto)
    //             .when().post("/users/{userId}/certificates", "any");

    //     response.then().assertThat().statusCode(404);

    // }

    @Test
    public void shouldReturnACertificateDtoWithResponse200() {
        HasCertificate hasCertificate = Instancio.create(HasCertificate.class);

        doReturn(hasCertificate).when(certificateControllerService).saveCertificateForUser(any(String.class), any(CertificateDto.class));

        MockMvcResponse response = given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .body(Instancio.create(CertificateDto.class))
                .when().post("/users/{userId}/certificates", "any");

        response.then().assertThat().statusCode(200);
    }
}
