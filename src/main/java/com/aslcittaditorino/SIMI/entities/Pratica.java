package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@SequenceGenerator(name="pratica",initialValue = 1 )
@Data
public class Pratica {
    @Id
    @GeneratedValue()
    private Long id;
    private Date dataSegnalazione;
    private Date dataRicezione;
    private String struttDenunciante;
    private Date dataRegistrazione;
    private Date dataArchiviazione;
    private String operatore;
    private Date dataSimi;
    private String stato;

    @OneToOne(optional = true)
    private Morsicatura morsicatura;

    @OneToMany
    private List<Diagnosi> diagnosi;

    @ManyToOne
    private Persona paziente;

    @OneToMany(mappedBy = "pratica")
    private List<Contatto> contatti;

    @OneToMany(mappedBy="pratica")
    private List<Provvedimento> provvedimenti;
}
