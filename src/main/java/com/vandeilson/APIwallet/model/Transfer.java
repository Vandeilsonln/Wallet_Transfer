package com.vandeilson.APIwallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_payer", nullable = false)
    private Long idPayer;

    @Column(name = "id_payee", nullable = false)
    private Long idPayee;

    @Column(columnDefinition = "DECIMAL(8,2) DEFAULT 0.0")
    private float amount;
}
