package com.vandeilson.APIwallet.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "lojista")
public class Shopkeeper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String CPF;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;
}
