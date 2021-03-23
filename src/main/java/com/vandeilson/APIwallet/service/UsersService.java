package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.PaymentAuthorization;
import com.vandeilson.APIwallet.model.Users;
import com.vandeilson.APIwallet.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersService {

    @Autowired
    RestTemplate authExterno;

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersResponseDTO> findAll() {
        return usersRepository.findAll().stream()
                .map(UsersResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<Users> getById(Long id) throws ExecutionException {
        return Optional.of( usersRepository.findById(id).orElseThrow(() -> new ExecutionException(String.format("User with Id %d not found", id))));
    }

    public Users registerNewUser(Users users) throws ExecutionException {
        verifyIfEmailIsAlreadyRegistered(users.getEmail());
        verifyIfCpfOrCnpjIsAlreadyRegistered(users.getCpfCnpj());
        verifyIfUserTypeIsCorrectWithCpf(users);
        verifyIfUserTypeIsCorrectWithCnpj(users);

        try {
            return usersRepository.save(users);
        } catch (ConstraintViolationException e){
            System.out.println(e.getMessage());
            throw new ExecutionException("Validation Error. Please, check all the fields and try again");
        }

    }

    public void updateUserInfo(Long id, Users users) throws ExecutionException {
        verifyIfExists(id);
        users.setId(id);
        usersRepository.save(users);
    }

    public void deleteUser(Long id) throws ExecutionException {
        verifyIfExists(id);
        usersRepository.deleteById(id);
    }

    @Transactional(rollbackFor = ExecutionException.class, timeout = 5)
    public void transferMoney(Long idPayer, Long idPayee, Float value) throws ExecutionException {
        Users payer = getById(idPayer).orElse(null);
        verifyIfPayerIsNotJuridica(Objects.requireNonNull(payer).getType());
        verifyIfPayerHasEnoughMoney(payer.getWalletAmount(), value);

        Users payee = getById(idPayee).orElse(null);

        authorizePayment();

        Float updatedWalletPayer = payer.getWalletAmount() - value;
        Float updatedWalletPayee = payee.getWalletAmount() + value;

        payer.setWalletAmount(updatedWalletPayer);
        payee.setWalletAmount(updatedWalletPayee);

        updateUserInfo(idPayer, payer);
        updateUserInfo(idPayee, payee);
    }

    private void verifyIfExists(Long id) throws ExecutionException {
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

    private void verifyIfUserTypeIsCorrectWithCpf(Users users) throws ExecutionException{
        if(users.getType().getDocumento() == "CPF" && users.getCpfCnpj().length() != 11){
            throw new ExecutionException("Error with CPF. Please, check the number and try again");
        }
    }

    private void verifyIfUserTypeIsCorrectWithCnpj(Users users) throws ExecutionException{
        if(users.getType().getDocumento() == "CNPJ" && users.getCpfCnpj().length() != 14){
            throw new ExecutionException("Error with CNPJ. Please, check the number and try again");
        }
    }

    private void verifyIfPayerHasEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted) throws ExecutionException {
        if (valueToBeDeducted > payerCurrentAmount) {
            throw new ExecutionException(String.format("This user does not have funds for this transaction. Total funds: %.2f", payerCurrentAmount));
        }
    }

    private void verifyIfPayerIsNotJuridica(UsersTiposEnums type) throws ExecutionException {
        if (type != UsersTiposEnums.fisica){
            throw new ExecutionException("Lojistas are not allowed to send money, only to receive");
        }
    }

    private void authorizePayment() throws ExecutionException {
        PaymentAuthorization returnedMessage = authExterno.getForObject("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", PaymentAuthorization.class);
        assert returnedMessage != null;
        if (!returnedMessage.message.equals("Autorizado")){
            throw new ExecutionException("Transaction not authorized");
        }

    }
}
