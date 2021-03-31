package com.vandeilson.APIwallet.model.dto.request;

import com.vandeilson.APIwallet.model.entity.Users;
import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRequestDTO {

    private String fullName;
    private String cpfCnpj;
    private String email;
    private String senha;
    private Float walletAmount;
    private UsersTiposEnums type;

    public Users toModel(){
        Users users = new Users()
                .setFullName(this.fullName)
                .setCpfCnpj(this.cpfCnpj.replaceAll("[^0-9]+", ""))
                .setEmail(this.email)
                .setSenha(this.senha)
                .setWalletAmount(this.walletAmount)
                .setType(this.type);

        return users;
    }
}
