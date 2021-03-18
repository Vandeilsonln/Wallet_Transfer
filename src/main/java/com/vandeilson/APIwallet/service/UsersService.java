package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> getById(Long id) {
        verifyIfExists(id);
        return usersRepository.findById(id);
    }

    private void verifyIfExists(Long id){
        usersRepository.findById(id);
    }

    public Users registerNewUser(Users users) {
        return usersRepository.save(users);
    }

    public Users updateUserInfo(Long id, Users users) {
        users.setId(id);
        return usersRepository.save(users);
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
