package com.aslcittaditorino.SIMI.entities;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@SequenceGenerator(initialValue = 1,name="idgen",sequenceName = "contattoId",allocationSize = 1)
public class Contatto extends DataObject{


    @ManyToOne
    private Pratica pratica;
    @ManyToOne
    private Persona persona;
    private String relazione;
    private Long hbsAg;
    private Long antiHBs;
    private Long antiHBc;
    private Long antiHBcIgM;
    private Long antiHBe;
    private Long antiHCV;
}
