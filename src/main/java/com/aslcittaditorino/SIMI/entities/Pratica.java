package com.aslcittaditorino.SIMI.entities;

import lombok.Data;
import org.hibernate.type.BlobType;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String operatore;
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
        if(this.contatti==null)
            this.contatti=new ArrayList<>();
        if(!this.contatti.contains(contatto))
            contatti.add(contatto);
    }

    public void addProvvedimento(Provvedimento provvedimento){
        if(this.provvedimenti == null)
            this.provvedimenti=new ArrayList<>();
        if(!this.provvedimenti.contains(provvedimento))
            provvedimenti.add(provvedimento);
    }

    public void addDiagnosi(Diagnosi diagnosi){
        if(this.diagnosi==null)
            this.diagnosi = new ArrayList<>();
        if(!this.diagnosi.contains(diagnosi))
            this.diagnosi.add(diagnosi);
    }

    public void removeContatto(Contatto contatto){
        if(this.contatti==null)
            this.contatti=new ArrayList<>();
        if(this.contatti.contains(contatto))
            contatti.remove(contatto);
    }

    public void removeProvvedimento(Provvedimento provvedimento){
        if(this.provvedimenti == null)
            this.provvedimenti=new ArrayList<>();
        if(this.provvedimenti.contains(provvedimento))
            provvedimenti.remove(provvedimento);
    }

    public void removeDiagnosi(Diagnosi diagnosi){
        if(this.diagnosi==null)
            this.diagnosi = new ArrayList<>();
        if(this.diagnosi.contains(diagnosi))
            this.diagnosi.remove(diagnosi);
    }

    public void setPaziente(Persona paziente){
        this.paziente = paziente;
    }

    public void setMorsicatura(Morsicatura morsicatura){
        this.morsicatura = morsicatura;
    }
}
