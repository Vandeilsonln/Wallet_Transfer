package com.vandeilson.APIwallet.dto.request;

import com.vandeilson.APIwallet.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    private Long idPayer;
    private Long idPayee;
    private float amount;

    public Transfer toModel(){
        Transfer transfer = new Transfer()
                .setIdPayer(this.idPayer)
                .setIdPayee(this.idPayee)
                .setAmount(this.amount);

        return transfer;
    }
}
