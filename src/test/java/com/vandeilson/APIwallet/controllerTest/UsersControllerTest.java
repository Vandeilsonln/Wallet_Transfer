package com.vandeilson.APIwallet.controllerTest;

import com.vandeilson.APIwallet.controller.UsersController;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.service.UsersService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@WebMvcTest
public class UsersControllerTest {

    @Autowired
    private UsersController usersController;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    public void setup(){
        standaloneSetup(this.usersController);
    }

    @Test
    public void shouldReturnSuccessWhenGetAllUsersById() throws ExecutionException {

        Mockito.when(this.usersService.getById(1L))
                .thenReturn(Optional.of(new Users(1L, "Vandeilson", "42183918829",
                        "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica)));

        given()
                .accept(ContentType.JSON)
        .when()
                .get("api/v1/users/byId/{id}", 1L)
        .then()
                .statusCode(HttpStatus.OK.value());
    }
}
