package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonUserRepository extends JpaRepository<Users, Long> {

}
