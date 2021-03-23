package com.vandeilson.APIwallet.model.enums;

import com.vandeilson.APIwallet.utils.CnpjGroup;
import com.vandeilson.APIwallet.utils.CpfGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersTiposEnums {
    fisica("Fisica", "CPF", CpfGroup.class),
    juridica("Juridica", "CNPJ", CnpjGroup.class);

    private final String descricao;
    private final String documento;
    private final Class<?> group;

    }
