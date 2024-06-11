package com.lautadev.microservice_account.controller;

import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.dto.UpdateBalanceDTO;
import com.lautadev.microservice_account.model.Account;
import com.lautadev.microservice_account.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private IAccountService accountServ;

    @PostMapping("/save")
    public ResponseEntity<?> saveAccount(@Valid @RequestBody Account account){
        accountServ.saveAccount(account);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<Account>> getAccounts(){
        return ResponseEntity.ok(accountServ.getAccounts());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Account> findAccount(@PathVariable Long id){
        Account account = accountServ.findAccount(id);
        if(account == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/get/accountAndUser/{id}")
    public ResponseEntity<AccountDTO> findAccountAndUser(@PathVariable Long id){
        AccountDTO accountDTO = accountServ.findAccountAndUser(id);
        if(accountDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(accountDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long idAccount){
        accountServ.deleteAccount(idAccount);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<AccountDTO> editAccount(@PathVariable Long idAccount, @Valid @RequestBody Account account){
        accountServ.editAccount(idAccount,account);
        return ResponseEntity.ok(accountServ.findAccountAndUser(idAccount));
    }

    @PutMapping("/updateBalance/{id}")
    public ResponseEntity<AccountDTO> updateBalance(@PathVariable Long id, @RequestBody UpdateBalanceDTO updateBalanceDTO){
        accountServ.updateBalance(id,updateBalanceDTO);
        AccountDTO accountDTO = accountServ.findAccountAndUser(id);
        if(accountDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(accountDTO);
    }

}
