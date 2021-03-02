package com.aslcittaditorino.SIMI.entities;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Contatto {


    @Id
    private Long id;
    @ManyToOne
    private Pratica pratica;
    @ManyToOne
    private Persona persona;

    private String causale;
    private Long hbsAg;
    private Long antiHBs;
    private Long antiHBc;
    private Long antiHBcIgM;
    private Long antiHBe;
    private Long antiHCV;

    private Long no;
}
