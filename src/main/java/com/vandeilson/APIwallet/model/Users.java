package com.vandeilson.APIwallet.model;

import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.utils.CnpjGroup;
import com.vandeilson.APIwallet.utils.CpfGroup;
import com.vandeilson.APIwallet.utils.UsersGroupSequenceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@GroupSequenceProvider(UsersGroupSequenceProvider.class)
@Accessors(chain = true)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome_completo")
    @NotEmpty
    private String fullName;

    @Column(nullable = false, name = "cpf_cnpj")
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    private String cpfCnpj;

    @Column(nullable = false)
    @NotEmpty
    private String email;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 2, max = 24)
    private String senha;

    @Column(columnDefinition = "DECIMAL(8,2) DEFAULT 0.0", name = "wallet_amount")
    private Float walletAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_type")
    private UsersTiposEnums type;
}
