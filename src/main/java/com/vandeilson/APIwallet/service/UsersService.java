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
        return Optional.of( usersRepository.findById(id).orElseThrow(() -> new ExecutionException("User not found")));
    }

    public Users registerNewUser(Users users) throws ExecutionException {
        verifyIfEmailIsAlreadyRegistered(users);
        verifyIfCpfOrCnpjIsAlreadyRegistered(users);
        return usersRepository.save(users);
//        try {
//            return usersRepository.save(users);
//        } catch (ConstraintViolationException e){
//            throw new ExecutionException("It is not possible to write CPF to a 'juridica' type OR CNPJ to 'fisica' type");
//        }

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
        verifyIfPayerIsNotLojista(Objects.requireNonNull(payer).getType());
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

    private void verifyIfEmailIsAlreadyRegistered(Users user) throws ExecutionException {
        Optional<Users> optUsersEmail =  usersRepository.findByEmail(user.getEmail());
        if (optUsersEmail.isPresent()){
            throw new ExecutionException("This e-mail is already registered");
        }
    }

    private void verifyIfCpfOrCnpjIsAlreadyRegistered(Users user) throws ExecutionException {
        Optional<Users> optUsersCpfCnpj = usersRepository.findByCpfCnpj(user.getCpfCnpj());
        if (optUsersCpfCnpj.isPresent()){
            throw new ExecutionException("This CPF or CNPJ is already registered");
        }
    }

    private void verifyIfPayerHasEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted) throws ExecutionException {
        if (valueToBeDeducted > payerCurrentAmount) {
            throw new ExecutionException(String.format("This user does not have funds for this transaction. Total funds: %.2f", payerCurrentAmount));
        }
    }

    private void verifyIfPayerIsNotLojista(UsersTiposEnums type) throws ExecutionException {
        if (type != UsersTiposEnums.fisica){
            throw new ExecutionException("Lojistas are not allowed to send money, only to receive");
        }
    }

    private void authorizePayment() throws ExecutionException {
        PaymentAuthorization returnedMessage = authExterno.getForObject("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", PaymentAuthorization.class);
        if (!returnedMessage.message.equals("Autorizado")){
            throw new ExecutionException("Transaction not authorized");
        }

    }
}
