package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
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
            this.contatti.add(contatto);
            contatto.setPersona(this);

    }

    public void addMorsicatura(Morsicatura morsicatura){
            this.morsicature.add(morsicatura);
            morsicatura.setProprietario(this);
    }

    public void addProvvedimenti(Provvedimento provvedimento){
            this.provvedimenti.add(provvedimento);
            provvedimento.setPersona(this);
    }

    public void addPratica(Pratica pratica){
            this.pratiche.add(pratica);
            pratica.setPaziente(this);

    }
}
