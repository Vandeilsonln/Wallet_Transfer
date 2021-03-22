package com.vandeilson.APIwallet.serviceTest;

import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersService usersService;

//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void whenRegisterNewUserThenShouldCreateUser() throws ExecutionException {

        Users user = new Users();
        when(usersService.registerNewUser(user)).thenReturn(new Users(1L, "Vandeilson","42183918829", "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica));
        assertEquals("Vandeilson", usersService.registerNewUser(user).getFullName());

    }

}
