package com.lautadev.microservice_account.Throwable;

import com.lautadev.microservice_account.model.Account;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AccountValidator {
    private final Validator validator;

    public AccountValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validate(Account account) {
        if (account == null) {
            throw new AccountException("Account cannot be null");
        }

        // Validaciones estándar usando Hibernate Validator
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Account> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new AccountException(sb.toString());
        }

    }
}
