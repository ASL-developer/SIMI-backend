package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
public class PersonaDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private String codF;

    @NotNull
    @NotEmpty
    private String nome;
    @NotNull
    @NotEmpty
    private String cognome;
    private String cellulare;
    @NotNull
    @NotEmpty
    private Date dataDiNascita;
    @NotNull
    @NotEmpty
    private char sesso;
    private String nazionalita;
    @NotNull
    @NotEmpty
    private String luogoDiNascita;
    private String residenza;
    private String comuneResidenza;
    private String domicilio;
    private String comuneDomicilio;
    private String professione;
    private String categoriaProfessionale;

}
