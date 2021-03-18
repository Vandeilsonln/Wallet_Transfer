package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.model.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {

}
