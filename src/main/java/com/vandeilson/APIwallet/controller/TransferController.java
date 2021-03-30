package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.dto.request.TransferRequestDTO;
import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Transfer;
import com.vandeilson.APIwallet.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transfers")
@AllArgsConstructor
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Operation(summary = "Retrieve all transactions from the database")
    @ApiResponse(responseCode = "200",
            description = "Ok",
            content = @Content)
    @GetMapping("/all")
    public List<Transfer> getAllTransfers() {return transferService.getAll();}

    @Operation(summary = "Retrieve all transactions made by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content)})
    @GetMapping("/payerId/{id}")
    public List<Transfer> getAllById(@PathVariable Long id) throws ExecutionException {
        return transferService.getByPayerId(id);
    }

    @Operation(summary = "Transfer money to other users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Money transferred with success"
            ),
            @ApiResponse(responseCode = "404",
                    description = "It is not possible to make this transaction. Either the payer have no funds" +
                            " or the payer is a 'juridica' user type.",
                    content = @Content)})
    @PutMapping("/send")
    public TransferRequestDTO transferMoney(@RequestBody TransferRequestDTO transferRequestDTO) throws ExecutionException {
        transferService.transferMoney(transferRequestDTO.toModel());
        return transferRequestDTO;
    }
}
