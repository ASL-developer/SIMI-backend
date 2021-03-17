package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@SequenceGenerator(initialValue = 1,name="idgen",sequenceName = "personaId")

public class Persona extends DataObject{


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


    @OneToMany(mappedBy = "persona")
    private List<Provvedimento> provvedimenti;

    @OneToMany(mappedBy = "proprietario")
    private List<Morsicatura> morsicature;

    @OneToMany(mappedBy = "persona")
    private List<Contatto> contatti;

    @OneToMany(mappedBy = "paziente")
    private List<Pratica> pratiche;

    public void addContatto(Contatto contatto){
        if(this.contatti==null)
            this.contatti=new ArrayList<Contatto>();
        if(!this.contatti.contains(contatto))
            this.contatti.add(contatto);
    }

    public void addMorsicatura(Morsicatura morsicatura){
        if(this.morsicature==null)
            this.morsicature = new ArrayList<Morsicatura>();
        if(!this.morsicature.contains(morsicatura))
            this.morsicature.add(morsicatura);
    }

    public void addProvvedimento(Provvedimento provvedimento){
        if(this.provvedimenti==null)
            this.provvedimenti = new ArrayList<Provvedimento>();
        if(!this.provvedimenti.contains(provvedimento))
            this.provvedimenti.add(provvedimento);
    }

    public void addPratica(Pratica pratica){
        if(this.pratiche==null)
            this.pratiche = new ArrayList<>();
        if(!this.pratiche.contains(pratica))
            this.pratiche.add(pratica);
    }
}
