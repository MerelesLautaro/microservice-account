package com.lautadev.microservice_account.service;

import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.dto.UpdateBalanceDTO;
import com.lautadev.microservice_account.model.Account;
import com.lautadev.microservice_account.model.TypeOfOperation;

import java.util.List;

public interface IAccountService {
    public void saveAccount(Account account);
    public List<Account> getAccounts();
    public AccountDTO findAccountAndUser(Long idAccount);
    public Account findAccount(Long idAccount);
    public Account findByAlias(String alias);
    public Account findByCvu(String cvu);
    public void deleteAccount(Long idAccount);
    public void editAccount(Long idAccount, Account account);
    public void updateBalance(Long idAccount, UpdateBalanceDTO updateBalanceDTO,String methodOverride);
}
