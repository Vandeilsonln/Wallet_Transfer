package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.dto.request.UsersRequestDTO;
import com.vandeilson.APIwallet.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "Retrieve all Users currently registered into the application")
    @ApiResponse(responseCode = "200",
            description ="Ok",
            content = @Content)
    @GetMapping("/all")
    public List<UsersResponseDTO> getAllUsers(){return usersService.getAll();}

    @Operation(summary = "Retrieve a specific user from Database by giving its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "200",
            description = "User retrieved with success",
            content = @Content)})
    @GetMapping("/byId/{id}")
    public UsersResponseDTO getById(@PathVariable Long id) throws ExecutionException {
        return new UsersResponseDTO(Objects.requireNonNull(usersService.getById(id).orElse(null)));
    }

    @Operation(summary = "Register a new user into Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
            description = "User registered with success!",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Something went wrong. Either the CPF/CNPJ is invalid/Not compatible with user type" +
                    " or is already registered",
            content = @Content)})
    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersRequestDTO registerUser(@RequestBody UsersRequestDTO usersRequestDTO) throws ExecutionException {
        this.usersService.registerNewUser(usersRequestDTO.toModel());
        return usersRequestDTO;
    }

    @Operation(summary = "Update user from Database, once given its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "User not found or any field might be with a invalid input. Please, check" +
                            " all fields and try again",
                    content = @Content),
            @ApiResponse(responseCode = "200",
                    description = "User data updated with success",
                    content = {@Content(mediaType = "application/json")})})
    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UsersRequestDTO usersRequestDTO) throws ExecutionException {
        usersService.updateUserInfo(id, usersRequestDTO.toModel());
    }

    @Operation(summary = "Delete user from Database, once given its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Deleted with success",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
            content = @Content)})
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ExecutionException {
        usersService.deleteUser(id);
    }

    @Operation(summary = "Transfer money to other users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Money transferred with success"
                    ),
            @ApiResponse(responseCode = "404",
                    description = "It is not possible to make this transaction. Either the payer have no funds" +
                            " or the payer is a 'juridica' user type.",
                    content = @Content)})
    @PutMapping("/transfer/{idPayer}/{idPayee}/{value}")
    public void transferMoney(@PathVariable Long idPayer, @PathVariable Long idPayee,
                              @PathVariable Float value) throws ExecutionException {
        usersService.transferMoney(idPayer, idPayee, value);
    }

}
