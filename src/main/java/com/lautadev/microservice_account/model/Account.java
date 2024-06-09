package com.lautadev.microservice_account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccount;
    @Temporal(TemporalType.DATE)
    @NotNull
    @Past
    private LocalDate dateOfCreation;
    private double balance;
    @NotBlank
    private String cvu;
    @NotBlank
    private String alias;
    private Long idUser;
}
