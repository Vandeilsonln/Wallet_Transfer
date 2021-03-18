package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.model.CommonUser;
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

    public List<CommonUser> findAll() {
        return commonUserRepository.findAll();
    }

    public Optional<CommonUser> getById(Long id) {
        verifyIfExists(id);
        return commonUserRepository.findById(id);
    }

    private void verifyIfExists(Long id){
        commonUserRepository.findById(id);
    }

    public CommonUser registerNewUser(CommonUser commonUser) {
        return commonUserRepository.save(commonUser);
    }

    public CommonUser updateUserInfo(Long id, CommonUser commonUser) {
        commonUser.setId(id);
        return commonUserRepository.save(commonUser);
    }

    public void deleteUser(Long id) {
        commonUserRepository.deleteById(id);
    }
}
