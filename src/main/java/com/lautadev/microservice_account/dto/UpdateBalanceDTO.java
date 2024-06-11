package com.lautadev.microservice_account.dto;

import com.lautadev.microservice_account.model.TypeOfOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceDTO {
    private double amount;
    private TypeOfOperation typeOfOperation;
}
