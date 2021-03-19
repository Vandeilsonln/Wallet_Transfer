package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.exceptions.EmailOrCpfAlreadyRegisteredException;
import com.vandeilson.APIwallet.exceptions.LojistaCanNotTransferMoneyException;
import com.vandeilson.APIwallet.exceptions.PayerDoesNotHaveEnoughMoney;
import com.vandeilson.APIwallet.exceptions.UserNotFoundException;
import com.vandeilson.APIwallet.model.AuthPayment;
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

    @Transactional(rollbackFor = LojistaCanNotTransferMoneyException.class)
    public void transferMoney(Long idPayer, Long idPayee, Float value) throws UserNotFoundException, LojistaCanNotTransferMoneyException, PayerDoesNotHaveEnoughMoney {
        verifyIfExists(idPayer);
        verifyIfExists(idPayee);

        verifyIfPayerIsNotLojista(idPayer);

        Users payer = getById(idPayer).orElse(null);
        verifyIfPayerHasEnoughMoney(payer.getWalletAmount(), value);

        Users payee = getById(idPayee).orElse(null);

        authPayment();

        Float updatedWalletPayer = payer.getWalletAmount() - value;
        Float updatedWalletPayee = payee.getWalletAmount() + value;

        payer.setWalletAmount(updatedWalletPayer);
        payee.setWalletAmount(updatedWalletPayee);

        updateUserInfo(idPayer, payer);
        updateUserInfo(idPayee, payee);
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

    private void verifyIfPayerHasEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted) throws PayerDoesNotHaveEnoughMoney {
        if (valueToBeDeducted > payerCurrentAmount) {
            throw new PayerDoesNotHaveEnoughMoney(payerCurrentAmount, valueToBeDeducted);
        }
    }

    private void verifyIfPayerIsNotLojista(Long id) throws LojistaCanNotTransferMoneyException {
        Users optUser = usersRepository.findById(id).orElse(null);

        assert optUser != null;
        if (optUser.getType() != UsersTiposEnums.common){
            throw new LojistaCanNotTransferMoneyException();
        }
    }

    private AuthPayment authPayment(){
        return authExterno.getForObject("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", AuthPayment.class);

    }
}
