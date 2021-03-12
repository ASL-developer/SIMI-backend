package com.aslcittaditorino.SIMI.DTOs;

import com.aslcittaditorino.SIMI.entities.Persona;
import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class ProvvedimentoDTO {
    private Long id;
    private String tipo;
    private Date dataProvvedimento;
    private int numeroPersone;
    private Long dosaggio;
    private String codfProprietario;
}
