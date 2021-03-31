package com.vandeilson.APIwallet.controllerTest;

import com.vandeilson.APIwallet.controller.TransferController;
import com.vandeilson.APIwallet.controller.UsersController;
import com.vandeilson.APIwallet.model.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.entity.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.model.service.UsersService;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
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

    @MockBean
    private TransferController transferController;

    @BeforeEach
    public void setup(){
        standaloneSetup(this.usersController);
    }

    @Test
    public void shouldReturnSuccessWhenGetUserById() throws ExecutionException {

        Mockito.when(this.usersService.getById(1L))
                .thenReturn(Optional.of(new Users(1L, "Vandeilson", "11161917098",
                        "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica)));

        given()
                .accept(ContentType.JSON)
        .when()
                .get("api/v1/users/byId/{id}", 1L)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    public void shouldReturnNoContentWhenDeleteUser(){
        given()
                .accept(ContentType.JSON)
        .when()
                .delete("api/v1/users/delete/{id}", 1L)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    public void shouldReturnCreatedWhenRegisterNewUser(){

        JSONObject request = new JSONObject();

        request.put("fullName","Vandeilson");
        request.put("cpfCnpj","74343980000161");
        request.put("email", "email@email.com.br");
        request.put("senha", "123abc");
        request.put("walletAmount", 1000f);
        request.put("type", "juridica");

        given()
                .header("Content-Type", "application/json")
                .body(request.toJSONString())
        .when()
                .post("api/v1/users/registerUser")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();

    }

    @Test
    public void shouldReturnOkWhenUpdateUser(){

        JSONObject request = new JSONObject();

        request.put("fullName","Vandeilson");
        request.put("cpfCnpj","74343980000161");
        request.put("email", "email@email.com.br");
        request.put("senha", "123abc");
        request.put("walletAmount", 1000f);
        request.put("type", "juridica");

        given()
                .header("Content-Type", "application/json")
                .body(request.toJSONString())
                .when()
                .put("api/v1/users/update/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();

    }


}