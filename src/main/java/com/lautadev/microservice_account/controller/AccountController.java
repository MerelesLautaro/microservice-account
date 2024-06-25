package com.lautadev.microservice_account.controller;

import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.dto.UpdateBalanceDTO;
import com.lautadev.microservice_account.model.Account;
import com.lautadev.microservice_account.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/get/alias/{alias}")
    public ResponseEntity<Account> findByAlias(@PathVariable String alias){
        Account account  = accountServ.findByAlias(alias);
        if(account == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/get/cvu/{cvu}")
    public ResponseEntity<Account> findByCvu(@PathVariable String cvu){
        Account account  = accountServ.findByCvu(cvu);
        if(account == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<Account> findAccountByUser(@PathVariable Long id){
        Account account = accountServ.findAccountByUser(id);
        if(account == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
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

    @PostMapping("/updateBalance/{id}")
    public ResponseEntity<AccountDTO> updateBalance(@PathVariable Long id, @RequestBody UpdateBalanceDTO updateBalanceDTO,
                                                    @RequestHeader(value = "X-HTTP-Method-Override", required = false)
                                                    String methodOverride){
        if ("PATCH".equalsIgnoreCase(methodOverride)) {
            accountServ.updateBalance(id, updateBalanceDTO, methodOverride);
            AccountDTO accountDTO = accountServ.findAccountAndUser(id);
            if (accountDTO == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(accountDTO);
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

}
