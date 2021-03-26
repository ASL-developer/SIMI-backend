package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@SequenceGenerator(initialValue = 1,name="idgen",sequenceName = "morsicaturaId",allocationSize = 1)
public class Morsicatura extends DataObject{

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

    @OneToOne(optional = true)
    private Pratica pratica;
}
