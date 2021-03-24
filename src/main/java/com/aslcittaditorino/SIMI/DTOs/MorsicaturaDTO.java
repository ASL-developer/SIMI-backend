package com.aslcittaditorino.SIMI.DTOs;

import com.aslcittaditorino.SIMI.entities.Persona;
import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MorsicaturaDTO {
    private Long id;

    @NotNull
    @NotEmpty
    private Date dataMorsicatura;
    private String animale;
    private String sedeLesione;
    @NotNull
    @NotEmpty
    private String gradoEsposizione;
    private String luogoEvento;
    private String rischio;
    private Date dataVaccRab;
    private Date dataVaccTet;
    private Long immRab;
    private Long immTet;
    private String note;

}
