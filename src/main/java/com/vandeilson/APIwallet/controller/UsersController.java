package com.vandeilson.APIwallet.controller;

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
    public Users getById(@PathVariable Long id){
        return usersService.getById(id).orElse(null);
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public Users registerUser(@RequestBody Users users){
        return usersService.registerNewUser(users);
    }

    @PutMapping("/update/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users users){
        return usersService.updateUserInfo(id, users);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        usersService.deleteUser(id);
    }


}
