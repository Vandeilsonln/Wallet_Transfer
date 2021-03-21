package com.vandeilson.APIwallet.model.enums;

import com.vandeilson.APIwallet.utils.CnpjGroup;
import com.vandeilson.APIwallet.utils.CpfGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersTiposEnums {
    fisica("Fisica", "CPF", "000.000.000-00", CpfGroup.class),
    juridica("Juridica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class);

    private final String descricao;
    private final String documento;
    private final String mascara;
    private final Class<?> group;

    }
