package com.vandeilson.APIwallet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersTiposEnums {
    lojista("lojista"),
    common("common");

    private final String description;
}
