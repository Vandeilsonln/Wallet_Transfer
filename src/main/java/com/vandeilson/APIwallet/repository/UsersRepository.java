package com.vandeilson.APIwallet.repository;

import com.vandeilson.APIwallet.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByCpf(String cpf);
}
