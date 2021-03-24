package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.dto.request.UsersRequestDTO;
import com.vandeilson.APIwallet.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.service.UsersService;
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

    @GetMapping("/all")
    public List<UsersResponseDTO> getAllCommonUsers(){return usersService.findAll();}

    @GetMapping("/byId/{id}")
    public UsersResponseDTO getById(@PathVariable Long id) throws ExecutionException {
        return new UsersResponseDTO(Objects.requireNonNull(usersService.getById(id).orElse(null)));
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersRequestDTO registerUser(@RequestBody UsersRequestDTO usersRequestDTO) throws ExecutionException {
        this.usersService.registerNewUser(usersRequestDTO.toModel());
        return usersRequestDTO;
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UsersRequestDTO usersRequestDTO) throws ExecutionException {
        usersService.updateUserInfo(id, usersRequestDTO.toModel());
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ExecutionException {
        usersService.deleteUser(id);
    }

    @PutMapping("/transfer/{idPayer}/{idPayee}/{value}")
    public void transferMoney(@PathVariable Long idPayer, @PathVariable Long idPayee,
                              @PathVariable Float value) throws ExecutionException {
        usersService.transferMoney(idPayer, idPayee, value);
    }

}
