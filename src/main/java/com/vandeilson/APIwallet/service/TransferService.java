package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Transfer;
import com.vandeilson.APIwallet.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

       public void registerNewTransfer(Transfer transfer){
        transferRepository.save(transfer);
    }

    public List<Transfer> getAll(){
        return transferRepository.findAll();
    }

    public List<Transfer> getByPayerId(Long payerId) throws ExecutionException {
        verifyIfExists(payerId);
        return transferRepository.getAllTransferById(payerId);
    }

    private void verifyIfExists(Long payerId) throws ExecutionException{
           transferRepository.findById(payerId).orElseThrow(() -> new ExecutionException(String.format("No user found with id %d", payerId)));
    }
}
