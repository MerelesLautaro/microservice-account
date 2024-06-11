package com.lautadev.microservice_account.service;

import com.lautadev.microservice_account.Throwable.AccountValidator;
import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.dto.UpdateBalanceDTO;
import com.lautadev.microservice_account.dto.UserDTO;
import com.lautadev.microservice_account.model.Account;
import com.lautadev.microservice_account.model.TypeOfOperation;
import com.lautadev.microservice_account.repository.IAccountRepository;
import com.lautadev.microservice_account.repository.IUserAPIClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements IAccountService{
    @Autowired
    private IAccountRepository accountRepo;

    @Autowired
    private IUserAPIClient userAPI;

    @Autowired
    private AccountValidator validator;

    @Override
    public void saveAccount(Account account) {
        validator.validate(account);
        accountRepo.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepo.findAll();
    }

    @Override
    @CircuitBreaker(name = "microservice-user",fallbackMethod = "fallBackFindAccount")
    @Retry(name = "microservice-user")
    public AccountDTO findAccountAndUser(Long idAccount) {
        Account account = accountRepo.findById(idAccount).orElse(null);
        assert account != null;
        UserDTO userDTO = userAPI.findUserAndBenefit(account.getIdUser());
        return (new AccountDTO(userDTO,account));
    }

    @Override
    public Account findAccount(Long idAccount) {
        return accountRepo.findById(idAccount).orElse(null);
    }

    public AccountDTO fallBackFindAccount(Throwable throwable) { return new AccountDTO();}

    @Override
    public void deleteAccount(Long idAccount) {
        accountRepo.deleteById(idAccount);
    }

    @Override
    public void editAccount(Long idAccount, Account account) {
        Account accountEdit = accountRepo.findById(idAccount).orElse(null);

        assert accountEdit != null;
        accountEdit.setDateOfCreation(account.getDateOfCreation());
        accountEdit.setAlias(account.getAlias());
        accountEdit.setCvu(account.getCvu());
        accountEdit.setBalance(account.getBalance());
        accountEdit.setIdUser(account.getIdUser());

        this.saveAccount(accountEdit);
    }

    @Override
    public void updateBalance(Long idAccount, UpdateBalanceDTO updateBalanceDTO) {
        Account account = this.findAccount(idAccount);
        if(updateBalanceDTO.getTypeOfOperation().equals(TypeOfOperation.MoneyReceived) ||
                updateBalanceDTO.getTypeOfOperation().equals(TypeOfOperation.BalanceTopUp)){
            double currentBalance = account.getBalance();
            currentBalance += updateBalanceDTO.getAmount();
            account.setBalance(currentBalance);
            this.saveAccount(account);
        }
    }
}
