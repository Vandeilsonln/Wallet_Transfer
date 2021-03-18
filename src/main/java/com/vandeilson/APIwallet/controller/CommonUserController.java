package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.model.CommonUser;
import com.vandeilson.APIwallet.service.CommonUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/commonuser")
@AllArgsConstructor
public class CommonUserController {

    @Autowired
    private CommonUserService commonUserService;

    @GetMapping("/all")
    public List<CommonUser> getAllCommonUsers(){
        return commonUserService.findAll();
    }

    @GetMapping("/byId/{id}")
    public CommonUser getById(@PathVariable Long id){
        return commonUserService.getById(id).orElse(null);
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonUser registerUser(@RequestBody CommonUser commonUser){
        return commonUserService.registerNewUser(commonUser);
    }

    @PutMapping("/update/{id}")
    public CommonUser updateUser(@PathVariable Long id, @RequestBody CommonUser commonUser){
        return commonUserService.updateUserInfo(id, commonUser);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        commonUserService.deleteUser(id);
    }
}
