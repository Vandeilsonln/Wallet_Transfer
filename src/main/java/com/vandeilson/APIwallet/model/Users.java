package com.vandeilson.APIwallet.model;

import com.vandeilson.APIwallet.enums.UsersTiposEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    public Users(String fullName, String cpf, String email, String senha, Float walletAmount, UsersTiposEnums type){
        super();
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.walletAmount = walletAmount;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column
    private Float walletAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UsersTiposEnums type;
}
