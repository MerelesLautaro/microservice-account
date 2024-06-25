package com.lautadev.microservice_account.service;

import com.lautadev.microservice_account.Throwable.AccountException;
import com.lautadev.microservice_account.Throwable.AccountValidator;
import com.lautadev.microservice_account.dto.AccountDTO;
import com.lautadev.microservice_account.dto.BenefitDTO;
import com.lautadev.microservice_account.dto.UpdateBalanceDTO;
import com.lautadev.microservice_account.dto.UserDTO;
import com.lautadev.microservice_account.model.Account;
import com.lautadev.microservice_account.model.TypeOfOperation;
import com.lautadev.microservice_account.repository.IAccountRepository;
import com.lautadev.microservice_account.repository.IUserAPIClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements IAccountService{

    private final IAccountRepository accountRepo;
    private final IUserAPIClient userAPI;
    private final AccountValidator validator;

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(IAccountRepository accountRepo, IUserAPIClient userAPI, AccountValidator validator){
        this.accountRepo = accountRepo;
        this.userAPI = userAPI;
        this.validator = validator;
    }

    @Override
    @Transactional
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

    public AccountDTO fallBackFindAccount(Long idAccount, Throwable throwable) {
        Account defaultAccount = new Account();
        defaultAccount.setIdAccount(idAccount);
        defaultAccount.setIdUser(-1L);

        UserDTO defaultUser = new UserDTO();
        defaultUser.setIdUser(-1L);
        defaultUser.setName("Unknown User");
        defaultUser.setBenefit(null);

        return new AccountDTO(defaultUser, defaultAccount);
    }


    @Override
    public Account findAccount(Long idAccount) {
        return accountRepo.findById(idAccount).orElse(null);
    }

    @Override
    public Account findAccountByUser(Long idUser) {
        return accountRepo.findAccountByIdUser(idUser).orElse(null);
    }

    @Override
    public Account findByAlias(String alias) {
        return accountRepo.findByAlias(alias).orElse(null);
    }

    @Override
    public Account findByCvu(String cvu) {
        return accountRepo.findByCvu(cvu).orElse(null);
    }

    @Override
    @Transactional
    public void deleteAccount(Long idAccount) {
        accountRepo.deleteById(idAccount);
    }

    @Override
    @Transactional
    public void editAccount(Long idAccount, Account account) {
        Account accountEdit = accountRepo.findById(idAccount).orElse(null);
        assert accountEdit != null;
        BeanUtils.copyProperties(account,accountEdit,"idAccount");
        this.saveAccount(accountEdit);
    }

    @Override
    @Transactional
    public void updateBalance(Long idAccount, UpdateBalanceDTO updateBalanceDTO, String methodOverride) {
        Account account = this.findAccount(idAccount);
        switch (updateBalanceDTO.getTypeOfOperation()) {
            case MoneyReceived:
            case BalanceTopUp:
                increaseBalance(account, updateBalanceDTO.getAmount());
                break;
            case WithDrawalOfMoney:
                decreaseBalance(account, updateBalanceDTO.getAmount());
                break;
            case MoneyTransfer:
                handleMoneyTransfer(account, updateBalanceDTO);
                break;
            case QRpayment:
                handleQRPayment(account, updateBalanceDTO);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation type: " + updateBalanceDTO.getTypeOfOperation());
        }
    }

    private void increaseBalance(Account account, double amount) {
        double currentBalance = account.getBalance();
        currentBalance += amount;
        account.setBalance(currentBalance);
        this.saveAccount(account);
    }

    private void decreaseBalance(Account account, double amount) {
        double currentBalance = account.getBalance();
        currentBalance -= amount;
        account.setBalance(currentBalance);
        this.saveAccount(account);
    }

    private void handleMoneyTransfer(Account account, UpdateBalanceDTO updateBalanceDTO) {
        Account destinationAccount = findDestinationAccount(updateBalanceDTO.getAliasOrCvu());
        if (destinationAccount != null) {
            increaseBalance(destinationAccount, updateBalanceDTO.getAmount());
            decreaseBalance(account, updateBalanceDTO.getAmount());
        } else {
            throw new AccountException("Destination account not found");
        }
    }

    private Account findDestinationAccount(String aliasOrCvu) {
        Account account = this.findByAlias(aliasOrCvu);
        if (account == null) {
            account = this.findByCvu(aliasOrCvu);
        }
        return account;
    }

    @CircuitBreaker(name = "microservice-user", fallbackMethod = "fallBackForHandleQRPayment")
    @Retry(name = "microservice-user")
    private void handleQRPayment(Account account, UpdateBalanceDTO updateBalanceDTO) {
        AccountDTO accountDTO = this.findAccountAndUser(account.getIdUser());
        UserDTO userDTO = accountDTO.getUser();
        BenefitDTO benefitDTO = userDTO.getBenefit();

        if (benefitDTO != null && userDTO.getTickets() >= 1) {
            userAPI.updateTickets(account.getIdUser(), 1, "PATCH");
        } else {
            decreaseBalance(account, updateBalanceDTO.getAmount());
        }
    }


    public void fallBackForHandleQRPayment(Account account, UpdateBalanceDTO updateBalanceDTO, Throwable throwable) {
        logger.error("Error in handleQRPayment for account id: {} with amount: {}, error: {}",
                account.getIdAccount(), updateBalanceDTO.getAmount(), throwable.getMessage());
        decreaseBalance(account, updateBalanceDTO.getAmount());
    }

}
