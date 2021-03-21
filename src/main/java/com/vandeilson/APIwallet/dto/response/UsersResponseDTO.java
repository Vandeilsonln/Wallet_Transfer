package com.vandeilson.APIwallet.dto.response;

import com.vandeilson.APIwallet.enums.UsersTiposEnums;
import com.vandeilson.APIwallet.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDTO {

    private Long id;
    private String fullName;
    private String cpf;
    private String email;
    private UsersTiposEnums type;

    public UsersResponseDTO (Users users){
        this.id = users.getId();
        this.fullName = users.getFullName();
        this.cpf = users.getCpf();
        this.email = users.getEmail();
        this.type = users.getType();
    }
}
