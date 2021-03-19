package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.exceptions.EmailOrCpfAlreadyRegisteredException;
import com.vandeilson.APIwallet.exceptions.UserNotFoundException;
import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/commonuser")
@AllArgsConstructor
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/all")
    public List<Users> getAllCommonUsers(){
        return usersService.findAll();
    }

    @GetMapping("/byId/{id}")
    public Users getById(@PathVariable Long id) throws UserNotFoundException {
        return usersService.getById(id).orElse(null);
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public Users registerUser(@RequestBody Users users) throws EmailOrCpfAlreadyRegisteredException {
        return usersService.registerNewUser(users);
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Users users) throws UserNotFoundException {
        usersService.updateUserInfo(id, users);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws UserNotFoundException {
        usersService.deleteUser(id);
    }

}
