package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Users;
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
    public List<Users> getAllCommonUsers(){return usersService.findAll();}

    @GetMapping("/byId/{id}")
    public UsersResponseDTO getById(@PathVariable Long id) throws ExecutionException {
        return new UsersResponseDTO(Objects.requireNonNull(usersService.getById(id).orElse(null)));
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public Users registerUser(@RequestBody Users users) throws ExecutionException {
        return usersService.registerNewUser(users);
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Users users) throws ExecutionException {
        usersService.updateUserInfo(id, users);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ExecutionException {
        usersService.deleteUser(id);
    }

    @PutMapping("/transfer/{idPayer}/{idPayee}/{value}")
    public void transferMoney(@PathVariable Long idPayer, @PathVariable Long idPayee, @PathVariable Float value) throws ExecutionException {
        usersService.transferMoney(idPayer, idPayee, value);
    }

}
