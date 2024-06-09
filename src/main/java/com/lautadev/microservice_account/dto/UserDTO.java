package com.lautadev.microservice_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long idUser;
    private String name;
    private String surname;
    private String dni;
    private BenefitDTO benefit;
}
