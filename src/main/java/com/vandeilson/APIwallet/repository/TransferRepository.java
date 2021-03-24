package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query(value = "SELECT * FROM transfers WHERE id_payer = ?1", nativeQuery = true)
    public List<Transfer> getAllTransferById(Long id_payer);
}
