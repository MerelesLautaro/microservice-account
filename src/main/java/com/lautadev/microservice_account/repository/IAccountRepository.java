package com.lautadev.microservice_account.repository;

import com.lautadev.microservice_account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAlias(String alias);
    Optional<Account> findByCvu(String cvu);
    Optional<Account> findAccountByIdUser(Long idUser);
}
