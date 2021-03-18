package com.vandeilson.APIwallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "usuariocomum")
@AllArgsConstructor
@NoArgsConstructor
public class CommonUser {

    public CommonUser(String fullName, String cpf, String email, String senha, Float walletAmount){
        super();
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.walletAmount = walletAmount;
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
}
