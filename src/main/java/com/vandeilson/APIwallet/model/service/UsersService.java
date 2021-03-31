package com.vandeilson.APIwallet.model.service;

import com.vandeilson.APIwallet.model.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.model.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.entity.Users;
import com.vandeilson.APIwallet.model.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersResponseDTO> getAll() {
        return usersRepository.findAll().stream()
                .map(UsersResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<Users> getById(Long id) throws ExecutionException {
        return Optional.of( usersRepository.findById(id).orElseThrow(() -> new ExecutionException(String
                .format("User with Id %d not found", id))));
    }

    public Users registerNewUser(Users users) throws ExecutionException {
        verifyIfEmailIsAlreadyRegistered(users.getEmail());
        verifyIfCpfOrCnpjIsAlreadyRegistered(users.getCpfCnpj());
        verifyIfDocumentNumberIsCorrect(users);

        try {
            return usersRepository.save(users);
        } catch (ConstraintViolationException e) {
            System.out.println(e.getMessage());
            throw new ExecutionException("Validation Error. Please, check all the fields and try again");
        }
    }

    public void updateUserInfo(Long id, Users users) throws ExecutionException {
        verifyIfExists(id);
        verifyIfDocumentNumberIsCorrect(users);
        users.setId(id);
        usersRepository.save(users);
    }

    public void deleteUser(Long id) throws ExecutionException {
        verifyIfExists(id);
        usersRepository.deleteById(id);
    }

    public void verifyIfExists(Long id) throws ExecutionException {
        usersRepository.findById(id)
            .orElseThrow(() -> new ExecutionException(String.format("User with Id %d not found", id)));
    }

    private void verifyIfEmailIsAlreadyRegistered(String email) throws ExecutionException {
        Optional<Users> optUsersEmail =  usersRepository.findByEmail(email);
        if (optUsersEmail.isPresent()){
            throw new ExecutionException("This e-mail is already registered");
        }
    }

    private void verifyIfCpfOrCnpjIsAlreadyRegistered(String cpfCnpj ) throws ExecutionException {
        Optional<Users> optUsersCpfCnpj = usersRepository.findByCpfCnpj(cpfCnpj);
        if (optUsersCpfCnpj.isPresent()){
            throw new ExecutionException("This document number is already registered");
        }
    }

    private void verifyIfDocumentNumberIsCorrect(Users users) throws ExecutionException {
        verifyIfUserTypeIsCorrectWithCpf(users.getType().getDocumento(), users.getCpfCnpj().length());
        verifyIfUserTypeIsCorrectWithCnpj(users.getType().getDocumento(), users.getCpfCnpj().length());
    }

    private void verifyIfUserTypeIsCorrectWithCpf(String document, int size) throws ExecutionException{
        if(document.equals("CPF") && size != 11){
            throw new ExecutionException("Error with CPF. Please, check the number and try again");
        }
    }

    private void verifyIfUserTypeIsCorrectWithCnpj(String document, int size) throws ExecutionException{
        if(document.equals("CNPJ") && size != 14){
            throw new ExecutionException("Error with CNPJ. Please, check the number and try again");
        }
    }
}
