package com.lautadev.microservice_account.dto;


import com.lautadev.microservice_account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long idAccount;
    private LocalDate dateOfCreation;
    private double balance;
    private String cvu;
    private String alias;
    private UserDTO user;

    public AccountDTO(UserDTO user, Account account) {
        this.idAccount = account.getIdAccount();
        this.dateOfCreation = account.getDateOfCreation();
        this.balance = account.getBalance();
        this.cvu = account.getCvu();
        this.alias = account.getAlias();
        this.user = user;
    }
}
