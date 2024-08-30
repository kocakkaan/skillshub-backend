package com.reply.skillshub.controllers.company;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.reply.skillshub.base.exceptionhandling.SkillhubExceptionHandler;
import com.reply.skillshub.base.exceptionhandling.exeptions.NoCompanyFound;
import com.reply.skillshub.openapi.model.CompanyDto;
import com.reply.skillshub.openapi.model.CreateCompanyRequest;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.response.MockMvcResponse;

@WebMvcTest(CompanyController.class)
@ContextConfiguration(classes = {CompanyController.class, SkillhubExceptionHandler.class})
@AutoConfigureMockMvc(addFilters=false)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyControllerService companyControllerService;

    @Test
    public void shouldReturn404WhenServiceThrowsNotFoundException() {
        doThrow(new NoCompanyFound()).when(companyControllerService).getCompaniesForCurrentUser();

        given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .when().get("/user/me/company")
            .then().assertThat().statusCode(404);
        
    }

    @Test
    public void shouldReturnAlistWithResponse200WhenServiceReturnsList() {
        doReturn(Instancio.createList(CompanyDto.class)).when(companyControllerService).getCompaniesForCurrentUser();

        MockMvcResponse response = given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .when().get("/user/me/company");
        
        response.then().assertThat().statusCode(200);

        Assertions.assertNotSame(0, response.as(CompanyDto[].class).length);
        
    }

    @Test
    public void shouldReturnAlistWithResponse200WhenSavedSuccessfully() {
        doReturn(Instancio.createList(CompanyDto.class)).when(companyControllerService).getCompaniesForCurrentUser();

        MockMvcResponse response = given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .body(Instancio.create(CreateCompanyRequest.class))
            .when().post("/user/me/company");
        
        response.then().assertThat().statusCode(200);
        
    }

    @Test
    public void shouldReturn400WhenNoBody() {
        doReturn(Instancio.createList(CompanyDto.class)).when(companyControllerService).getCompaniesForCurrentUser();

        MockMvcResponse response = given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .when().post("/user/me/company");
        
        response.then().assertThat().statusCode(400);
    }
    
}
