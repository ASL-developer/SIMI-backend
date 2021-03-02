package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import javax.persistence.Id;
import java.sql.Date;

@Data
public class PersonaDTO {
    private String codf;

    private String nome;
    private String cognome;
    private String cellulare;
    private Date dataDiNascita;
    private char sesso;
    private String nazionalita;
    private String luogoDiNascita;
    private String residenza;
    private String comuneResidenza;
    private String domicilio;
    private String comuneDomicilio;
    private String professione;
    private String categoriaProfessionale;

}
