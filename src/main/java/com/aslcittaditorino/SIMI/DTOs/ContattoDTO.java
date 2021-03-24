package com.aslcittaditorino.SIMI.DTOs;


import com.aslcittaditorino.SIMI.entities.Persona;
import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
public class ContattoDTO {

    private Long id;
    private String relazione;
    private Long hbsAg;
    private Long antiHBs;
    private Long antiHBc;
    private Long antiHBcIgM;
    private Long antiHBe;
    private Long antiHCV;
}
