package com.vandeilson.APIwallet.model.dto.response;

import com.vandeilson.APIwallet.model.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDTO {

    private Long id;
    private String fullName;
    private String cpfCnpj;
    private String email;
    private UsersTiposEnums type;

    public UsersResponseDTO (Users users){
        this.id = users.getId();
        this.fullName = users.getFullName();
        this.cpfCnpj = users.getCpfCnpj();
        this.email = users.getEmail();
        this.type = users.getType();
    }
}
