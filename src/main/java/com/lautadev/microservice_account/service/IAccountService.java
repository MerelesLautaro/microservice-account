package com.lautadev.microservice_account.service;

import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.model.Account;

import java.util.List;

public interface IAccountService {
    public void saveAccount(Account account);
    public List<Account> getAccounts();
    public AccountDTO findAccount(Long idAccount);
    public void deleteAccount(Long idAccount);
    public void editAccount(Long idAccount, Account account);
}
