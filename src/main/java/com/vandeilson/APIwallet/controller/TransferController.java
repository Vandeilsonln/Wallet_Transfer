package com.vandeilson.APIwallet.controller;

import com.vandeilson.APIwallet.exceptions.ExecutionException;
import com.vandeilson.APIwallet.model.Transfer;
import com.vandeilson.APIwallet.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
