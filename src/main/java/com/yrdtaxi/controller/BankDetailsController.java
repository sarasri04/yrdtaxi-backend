package com.yrdtaxi.controller;


import com.yrdtaxi.model.BankDetails;
import com.yrdtaxi.service.BankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-details")
@CrossOrigin(origins = "http://localhost:3000")
public class BankDetailsController {

    @Autowired
    private BankDetailsService bankDetailsService;

    @GetMapping
    public ResponseEntity<List<BankDetails>> getAllBankDetails() {
        return ResponseEntity.ok(bankDetailsService.getAllBankDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDetails> getBankDetailsById(@PathVariable Long id) {

        return bankDetailsService.getBankDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BankDetails> createBankDetails(
            @RequestBody BankDetails bankDetails) {

        return ResponseEntity.ok(
                bankDetailsService.saveBankDetails(bankDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankDetails> updateBankDetails(
            @PathVariable Long id,
            @RequestBody BankDetails bankDetails) {

        return ResponseEntity.ok(
                bankDetailsService.updateBankDetails(id, bankDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBankDetails(
            @PathVariable Long id) {

        bankDetailsService.deleteBankDetails(id);

        return ResponseEntity.ok("Bank Details Deleted Successfully");
    }
}


