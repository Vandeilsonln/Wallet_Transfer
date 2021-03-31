package com.vandeilson.APIwallet.model.service;

import com.vandeilson.APIwallet.model.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.utils.PaymentAuthorization;
import com.vandeilson.APIwallet.model.entity.Transfer;
import com.vandeilson.APIwallet.model.entity.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.model.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    RestTemplate authExterno;

    public List<Transfer> getAll(){
        return transferRepository.findAll();
    }

    public List<Transfer> getByPayerId(Long payerId) throws ExecutionException {
        verifyIfExists(payerId);
        return transferRepository.getAllTransferById(payerId);
    }

    @Transactional(rollbackFor = ExecutionException.class, timeout = 5)
    public void transferMoney(Transfer transfer) throws ExecutionException {

        if(transfer.getIdPayer().equals(transfer.getIdPayee())){
            throw new ExecutionException("It is not possible to transfer money to yourself");
        }

        Users payer = usersService.getById(transfer.getIdPayer()).orElse(null);
        Users payee = usersService.getById(transfer.getIdPayee()).orElse(null);

        verifyIfPayerIsAbleToTransfer(payer.getWalletAmount(), transfer.getAmount(),payer.getType());

        authorizePayment();

        payer.setWalletAmount(subtractMoneyFromPayer(payer.getWalletAmount(), transfer.getAmount()));
        payee.setWalletAmount(addMoneyToPayee(payee.getWalletAmount(), transfer.getAmount()));

        usersService.updateUserInfo(transfer.getIdPayer(), payer);
        usersService.updateUserInfo(transfer.getIdPayee(), payee);

        registerNewTransfer(transfer);
    }

    public void registerNewTransfer(Transfer transfer){
        transferRepository.save(transfer);
    }

    private void verifyIfExists(Long payerId) throws ExecutionException{
           transferRepository.findById(payerId).orElseThrow(() -> new ExecutionException(String.format("No user found with id %d", payerId)));
    }

    private void verifyIfPayerIsNotJuridica(UsersTiposEnums type) throws ExecutionException {
        if (type != UsersTiposEnums.fisica){
            throw new ExecutionException("Lojistas are not allowed to send money, only to receive");
        }
    }

    private Float addMoneyToPayee(Float walletAmount, float amount) {
        return walletAmount + amount;
    }

    private Float subtractMoneyFromPayer(Float walletAmount, float amount) {
        return walletAmount -amount;
    }

    private void authorizePayment() throws ExecutionException {
        PaymentAuthorization returnedMessage = authExterno
                .getForObject("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6",
                        PaymentAuthorization.class);
        assert returnedMessage != null;
        if (!returnedMessage.message.equals("Autorizado")){
            throw new ExecutionException("Transaction not authorized");
        }
    }

    private void verifyIfPayerIsAbleToTransfer(Float payerCurrentWalletAmount, Float valueToBeDeducted,
                                               UsersTiposEnums userType) throws ExecutionException {
        verifyIfPayerIsNotJuridica(Objects.requireNonNull(userType));
        verifyIfPayerHasEnoughMoney(payerCurrentWalletAmount, valueToBeDeducted);
    }

    private void verifyIfPayerHasEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted)throws ExecutionException {
        if (valueToBeDeducted > payerCurrentAmount) {
            throw new ExecutionException(String
                    .format("This user does not have funds for this transaction. Total funds: %.2f",
                            payerCurrentAmount));
        }
    }
}
