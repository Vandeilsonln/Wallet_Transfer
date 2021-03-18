package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.repository.CommonUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommonUserService {

    @Autowired
    private CommonUserRepository commonUserRepository;

    public List<Users> findAll() {
        return commonUserRepository.findAll();
    }

    public Optional<Users> getById(Long id) {
        verifyIfExists(id);
        return commonUserRepository.findById(id);
    }

    private void verifyIfExists(Long id){
        commonUserRepository.findById(id);
    }

    public Users registerNewUser(Users users) {
        return commonUserRepository.save(users);
    }

    public Users updateUserInfo(Long id, Users users) {
        users.setId(id);
        return commonUserRepository.save(users);
    }

    public void deleteUser(Long id) {
        commonUserRepository.deleteById(id);
    }
}
