package com.aslcittaditorino.SIMI.entities;

import lombok.Data;
import org.hibernate.type.BlobType;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
public class Pratica {

    @Id
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

    private BlobType pdf;

    public void addContatto(Contatto contatto){
            contatti.add(contatto);
        contatto.setPratica(this);
    }

    public void addProvvedimento(Provvedimento provvedimento){
            provvedimenti.add(provvedimento);
        provvedimento.setPratica(this);
    }

    public void addDiagnosi(Diagnosi diagnosi){
        this.diagnosi.add(diagnosi);
        diagnosi.setPratica(this);
    }

    public void setPaziente(Persona paziente){
        this.paziente = paziente;
            paziente.getPratiche().add(this);
    }

    public void setMorsicatura(Morsicatura morsicatura){
        this.morsicatura = morsicatura;
        morsicatura.setPratica(this);
    }
}
