package com.vandeilson.APIwallet.service;

import com.vandeilson.APIwallet.model.Transfer;
import com.vandeilson.APIwallet.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    public void registerNewTransfer(Transfer transfer){
        transferRepository.save(transfer);
    }
}
