package com.reply.skillshub.controllers.users;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.reply.skillshub.base.exceptionhandling.SkillhubExceptionHandler;
import com.reply.skillshub.data.company.Company;
import com.reply.skillshub.data.user.User;
import com.reply.skillshub.openapi.model.CreateUserRequest;
import com.reply.skillshub.openapi.model.UserConfirmRequest;

import io.restassured.http.ContentType;

@WebMvcTest(UsersController.class)
@ContextConfiguration(classes = {UsersController.class, SkillhubExceptionHandler.class})
@AutoConfigureMockMvc(addFilters=false)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersControllerService usersControllerService;

    @Test
    void createUserInCompany_withoutRequestBody() {
        Company company = Instancio.create(Company.class);


        given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .when().post("/company/{CompanyId}/employees", company.getId())
            .then().assertThat().statusCode(400);
    }

    @Test
    void createUserInCompany_withValidRequestBody() {
        Company company = Instancio.create(Company.class);


        given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .body(Instancio.create(CreateUserRequest.class))
            .when().post("/company/{CompanyId}/employees", company.getId())
            .then().assertThat().statusCode(201);
    }

    @Test
    void createUserInCompany_withEmptyPath() {
        given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .body(Instancio.create(CreateUserRequest.class))
            .when().post("/company/{CompanyId}/employees", "")
            .then().assertThat().statusCode(404);
    }

    @Test
    void sucessfull_confirmUser() {
        User user = Instancio.create(User.class);

        given()
            .mockMvc(mockMvc)
            .contentType(ContentType.JSON)
            .body(Instancio.create(UserConfirmRequest.class))
            .when().put("/users/{userId}/confirmation/{confirmationToken}", user.getId(), user.getConfirmationToken())
            .then().assertThat().statusCode(201);
    }

    
}
