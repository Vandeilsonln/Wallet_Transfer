package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.dto.response.UsersResponseDTO;
import com.vandeilson.APIwallet.enums.UsersTiposEnums;
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
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {

    @Autowired
    RestTemplate authExterno;

    @Autowired
    private UsersRepository usersRepository;

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> getById(Long id) throws ExecutionException {
        verifyIfExists(id);
        return usersRepository.findById(id);
    }

    public Users registerNewUser(Users users) throws ExecutionException {
        verifyIfEmailOrCPFIsAlreadyRegistered(users);
        return usersRepository.save(users);
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
        verifyIfExists(idPayer);
        verifyIfExists(idPayee);

        verifyIfPayerIsNotLojista(idPayer);

        Users payer = getById(idPayer).orElse(null);
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
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new ExecutionException("User not found"));
    }

    private void verifyIfEmailOrCPFIsAlreadyRegistered(Users user) throws ExecutionException {
        Optional<Users> optUsersEmail =  usersRepository.findByEmail(user.getEmail());
        Optional<Users> optUsersCpf = usersRepository.findByCpf(user.getCpf());

        if (optUsersEmail.isPresent() || optUsersCpf.isPresent()){
            throw new ExecutionException("Either the e-mail or the CPF is already registered");
        }
    }

    private void verifyIfPayerHasEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted) throws ExecutionException {
        if (valueToBeDeducted > payerCurrentAmount) {
            throw new ExecutionException("This user does not have funds for this transaction. Total funs: %f");
        }
    }

    private void verifyIfPayerIsNotLojista(Long id) throws ExecutionException {
        Users optUser = usersRepository.findById(id).orElse(null);

        assert optUser != null;
        if (optUser.getType() != UsersTiposEnums.common){
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
