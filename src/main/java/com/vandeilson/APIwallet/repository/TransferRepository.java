package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
