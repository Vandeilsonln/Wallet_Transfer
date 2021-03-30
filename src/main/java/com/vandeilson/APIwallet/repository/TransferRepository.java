package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query(value = "SELECT * FROM transfers WHERE id_payer = :id_payer", nativeQuery = true)
    List<Transfer> getAllTransferById(@Param("id_payer") Long id_payer);
}
