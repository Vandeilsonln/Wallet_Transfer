package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.exceptions.EmailOrCpfAlreadyRegisteredException;
import com.vandeilson.APIwallet.exceptions.UserNotFoundException;
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

    public Optional<Users> getById(Long id) throws UserNotFoundException {
        verifyIfExists(id);
        return usersRepository.findById(id);
    }

    public Users registerNewUser(Users users) throws EmailOrCpfAlreadyRegisteredException {
        verifyIfEmailOrCPFIsAlreadyRegistered(users);
        return usersRepository.save(users);
    }

    public void updateUserInfo(Long id, Users users) throws UserNotFoundException {
        verifyIfExists(id);
        users.setId(id);
        usersRepository.save(users);
    }

    public void deleteUser(Long id) throws UserNotFoundException{
        verifyIfExists(id);
        usersRepository.deleteById(id);
    }

    private void verifyIfExists(Long id) throws UserNotFoundException{
        usersRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfEmailOrCPFIsAlreadyRegistered(Users user) throws EmailOrCpfAlreadyRegisteredException {
        Optional<Users> optUsersEmail =  usersRepository.findByEmail(user.getEmail());
        Optional<Users> optUsersCpf = usersRepository.findByCpf(user.getCpf());

        if (optUsersEmail.isPresent() || optUsersCpf.isPresent()){
            throw new EmailOrCpfAlreadyRegisteredException();
        }
    }

}