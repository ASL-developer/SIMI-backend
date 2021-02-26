package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Data
public class Morsicatura {
    @Id
    private Long id;

    private Date dataMorsicatura;
    private String animale;
    private String sedeLesione;
    private String gradoEsposizione;
    private String luogoEvento;
    private String rischio;
    private Date dataVaccRab;
    private Date dataVaccTet;
    private Long immRab;
    private Long immTet;
    private String note;

    @ManyToOne
    private Persona proprietario;

    @OneToOne(optional = false)
    private Pratica pratica;
}
