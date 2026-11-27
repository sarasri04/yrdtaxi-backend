package com.yrdtaxi.service;


import com.yrdtaxi.model.BankDetails;
import com.yrdtaxi.repository.BankDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankDetailsService {

    @Autowired
    private BankDetailsRepository repository;

    public List<BankDetails> getAllBankDetails() {
        return repository.findAll();
    }

    public Optional<BankDetails> getBankDetailsById(Long id) {
        return repository.findById(id);
    }

    public BankDetails saveBankDetails(BankDetails bankDetails) {
        return repository.save(bankDetails);
    }

    public BankDetails updateBankDetails(Long id, BankDetails bankDetails) {

        BankDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Details Not Found"));

        existing.setAccountName(bankDetails.getAccountName());
        existing.setBankName(bankDetails.getBankName());
        existing.setAccountNumber(bankDetails.getAccountNumber());
        existing.setIfscCode(bankDetails.getIfscCode());
        existing.setBranch(bankDetails.getBranch());
        existing.setUpiId(bankDetails.getUpiId());
        existing.setQrCode(bankDetails.getQrCode());

        return repository.save(existing);
    }

    public void deleteBankDetails(Long id) {
        repository.deleteById(id);
    }
}


