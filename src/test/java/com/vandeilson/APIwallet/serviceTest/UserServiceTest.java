package com.vandeilson.APIwallet.serviceTest;

import com.vandeilson.APIwallet.dto.request.UsersRequestDTO;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.repository.UsersRepository;
import com.vandeilson.APIwallet.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;



    @Test
    public void whenRegisterNewUserThenShouldCreateUserWithId() throws ExecutionException {

        UsersRequestDTO user = new UsersRequestDTO("Vandeilson","42183918829",
                "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica);
        when(usersRepository.save(user.toModel())).thenReturn(new Users(1L, "Vandeilson",
                "42183918829", "email@email.com.br", "123abc",
                1000f, UsersTiposEnums.fisica));
        assertEquals(1L, usersService.registerNewUser(user.toModel()).getId());

    }

    @Test
    public void givenCnpjWithPunctuationThenShouldFormatToOnlyNumbers() throws ExecutionException {

        UsersRequestDTO user = new UsersRequestDTO("Nobre","86.733.107/0001-64",
                "emailjuridica@email.com.br", "456def", 1000f, UsersTiposEnums.juridica);
        when(usersRepository.save(user.toModel())).thenReturn(new Users(1L, "Vandeilson",
                "86733107000164", "email@email.com.br", "123abc",
                1000f, UsersTiposEnums.fisica));
        assertEquals("86733107000164", usersService.registerNewUser(user.toModel()).getCpfCnpj());

    }

    @Test
    public void givenCpfWithPunctuationThenShouldFormatToOnlyNumbers() throws ExecutionException {

        UsersRequestDTO user = new UsersRequestDTO("Nobre","111.619.170-98",
                "emailjuridica@email.com.br", "456def", 1000f, UsersTiposEnums.fisica);
        when(usersRepository.save(user.toModel())).thenReturn(new Users(1L, "Vandeilson",
                "11161917098", "email@email.com.br", "123abc",
                1000f, UsersTiposEnums.fisica));
        assertEquals("11161917098", usersService.registerNewUser(user.toModel()).getCpfCnpj());

    }

    @Test
    public void whenGetAllUsersShouldReturnAListOfUsers(){
        when(usersRepository.findAll()).thenReturn(Stream
                .of(new Users(1L, "Vandeilson","42183918829", "email@email.com.br",
                        "123abc", 1000f, UsersTiposEnums.fisica),
                        new Users(2L, "Nobre","74343980000161", "emailjuridica@email.com.br",
                                "456def", 1000f, UsersTiposEnums.juridica))
                .collect(Collectors.toList()));
        assertEquals(2,usersService.getAll().size());
    }

}
